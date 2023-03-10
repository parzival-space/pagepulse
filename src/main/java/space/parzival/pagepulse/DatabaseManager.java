package space.parzival.pagepulse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import space.parzival.pagepulse.properties.DatabaseProperties;
import space.parzival.pagepulse.properties.format.HistoryEntry;
import space.parzival.pagepulse.properties.format.Service;
import space.parzival.pagepulse.properties.format.Status;

@Slf4j
@Component
public class DatabaseManager {
  private Connection connection;
  private Statement statement;

  @Autowired
  public DatabaseManager(DatabaseProperties dbProperties) throws SQLException {
    // create database connection
    this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbProperties.getDatabasePath());
    this.statement = this.connection.createStatement();
    log.info("Database connected.");

    // check if initialization is required
  }

  public List<Service> getServices() {
    List<Service> services = new ArrayList<>();

    try {
      ResultSet rs = this.statement.executeQuery("SELECT * FROM services");

      while (rs.next()) {
        Service service = new Service();

        service.setId(rs.getInt("id"));
        service.setName(rs.getString("name"));
        service.setGroup(rs.getString("group"));
        service.setEndpoint(rs.getString("endpoint"));

        services.add(service);
      }
    }
    catch (SQLException e) {
      log.error("{}", e);
    }

    return services;
  }

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

  public void addHistoryEntry(int serviceId, Timestamp timestamp, Status status, String error) {
    try {
      this.statement.execute(
        "INSERT INTO history (serviceId, timestamp, status, error) " +
        "VALUES ('" + serviceId + "', '" + timestamp.toString() + 
          "', '" + status.toString() + 
          "', '" + (error == null ? "NULL" : error) + 
        "')"
      );
    }
    catch (SQLException e) {
      log.error("{}", e);
    }
  }

  public void addHistoryEntry(int serviceId, Timestamp timestamp, Status status) {
    this.addHistoryEntry(serviceId, timestamp, status, null);
  }

  public void addHistoryEntry(int serviceId, Status status) {
    this.addHistoryEntry(serviceId, new Timestamp(new Date().getTime()), status, null);
  }
}
