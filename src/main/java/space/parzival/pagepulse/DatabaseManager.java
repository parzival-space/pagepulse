package space.parzival.pagepulse;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.database.Status;
import space.parzival.pagepulse.properties.ApplicationProperties;
import space.parzival.pagepulse.properties.DatabaseProperties;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;

@Slf4j
@Component
public class DatabaseManager {
  private Connection connection;
  private Statement statement;

  @Autowired
  public DatabaseManager(DatabaseProperties dbProperties, ApplicationProperties properties) throws SQLException {
    boolean isNewDatabase = !(new File(dbProperties.getDatabasePath())).exists();

    // create database connection
    this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbProperties.getDatabasePath());
    this.statement = this.connection.createStatement();
    log.info("Database connected.");

    // enable foreign keys and check if initialization is required
    this.statement.execute("PRAGMA foreign_keys = ON");
    if (isNewDatabase) this.initTables();

    this.populateServices(properties);
  }

  private void initTables() throws SQLException {
    // initialize services table
    this.statement.execute(
      "CREATE TABLE services (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
        "\"group\" TEXT NOT NULL," +
        "name TEXT NOT NULL, " +
        "endpoint TEXT NOT NULL " +
      ")"
    );

    // initialize history table
    this.statement.execute(
      "CREATE TABLE history (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
        "serviceId INTEGER NOT NULL, "+
        "timestamp TIMESTAMP NOT NULL, "+
        "status VARCHAR(11) NOT NULL, "+
        "error TEXT, "+
        "FOREIGN KEY (serviceId) REFERENCES services(id) ON DELETE CASCADE" +
      ")"
    );
  }

  private void populateServices(ApplicationProperties properties) throws SQLException {
    List<ServiceConfiguration> serviceConfigurations = properties.getServices();
    if (serviceConfigurations.isEmpty()) {
      log.warn("No services registered in configuration file. The service table will empty.");
      this.statement.execute("DELETE FROM services");
      return;
    }

    log.info("Populating services table...");

    // remove obsolete services
    StringBuilder deleteStatement = new StringBuilder();
    deleteStatement.append("DELETE FROM services WHERE ");
    for (int i = 0; i < serviceConfigurations.size(); i++) {
      ServiceConfiguration sConf = serviceConfigurations.get(i);

      // update delete statement
      deleteStatement.append(
        String.format(
          "id NOT IN (SELECT id FROM services WHERE (name = '%s' AND \"group\" = '%s' AND endpoint = '%s'))", 
          sConf.getName(),
          sConf.getGroup(),
          sConf.getEndpoint()
        )
      );
      if (i +1 != serviceConfigurations.size()) deleteStatement.append(" AND ");
    }
    this.statement.execute(deleteStatement.toString());

    // create new entries
    int skipped = 0;
    StringBuilder insertStatement = new StringBuilder();
    insertStatement.append("INSERT INTO services (name, \"group\", endpoint) VALUES ");
    for (int i = 0; i < serviceConfigurations.size(); i++) {
      ServiceConfiguration sConf = serviceConfigurations.get(i);

      // make sure the service is not already registered
      if (this.getServiceId(sConf.getName(), sConf.getGroup()) != -1) {
        skipped++;
        continue;
      };

      // insert new services
      insertStatement.append(String.format("('%s', '%s', '%s')", sConf.getName(), sConf.getGroup(), sConf.getEndpoint()));
      if (i +1 != serviceConfigurations.size()) insertStatement.append(", ");
    }
    if (serviceConfigurations.size() - skipped == 0) {
      log.info("All service entries are up to date.");
    } else {
      log.info("Updating service entries...");
      this.statement.execute(insertStatement.toString());
    }
  }

  /**
   * Fetches the full service list from the database.
   * Could cause overhead on huge databases.
   * @return Full service list.
   */
  public List<Service> getServices() {
    List<Service> services = new ArrayList<>();

    try {
      ResultSet rs = this.statement.executeQuery("SELECT * FROM services");

      while (rs.next()) {
        Service service = new Service();

        service.setId(rs.getInt("id"));
        service.setName(rs.getString("name"));
        service.setGroup(rs.getString("group"));
        service.setEndpoint(new URI(rs.getString("endpoint")));

        services.add(service);
      }
    }
    catch (SQLException | URISyntaxException e) {
      log.error("{}", e);
    }

    return services;
  }

  /**
   * Fetches the full history of a service by its id.
   * @param serviceId The service you want to get the history for.
   * @return Full service history.
   */
  public List<HistoryEntry> getHistory(int serviceId) {
    List<HistoryEntry> history = new ArrayList<>();

    try {
      ResultSet rs = this.statement.executeQuery("SELECT * FROM history WHERE (serviceId = " + serviceId + ") ORDER BY timestamp DESC");

      while (rs.next()) {
        HistoryEntry entry = new HistoryEntry();

        entry.setTimestamp(rs.getTimestamp("timestamp"));
        entry.setError(rs.getString("error"));
        entry.parseStatus(rs.getString("status"));

        history.add(entry);
      }
    }
    catch (SQLException e) {
      log.error("{}", e);
    }

    return history;
  }

  /**
   * Returns the ID of a service or -1 if the Service could not be found.
   * @param name The name of the service.
   * @param group The group of the service.
   * @return The ID of a service or -1 if the Service could not be found.
   */
  public int getServiceId(String name, String group) {
    int result = -1;

    try {
      ResultSet rs = this.statement.executeQuery("SELECT id FROM services WHERE (name = '" + name + "' AND \"group\" = '" + group + "')");

      while (rs.next()) {
        result = rs.getInt("id");
      }
    }
    catch (SQLException e) {
      log.error("{}", e);
    }

    return result;
  }

  public void addHistoryEntry(int serviceId, Timestamp timestamp, Status status, String error) {
    try {
      this.statement.execute(
        "INSERT INTO history (serviceId, timestamp, status, error) " +
        "VALUES ('" + serviceId + "', '" + timestamp.toString() + 
          "', '" + status.toString() + 
          "', " + (error == null ? "NULL" : "'" + error + "'") + 
        ")"
      );
    }
    catch (SQLException e) {
      log.error("{}", e);
    }
  }
}
