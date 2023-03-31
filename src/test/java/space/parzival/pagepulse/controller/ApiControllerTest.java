package space.parzival.pagepulse.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import space.parzival.pagepulse.DatabaseManager;
import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.properties.ApplicationProperties;
import space.parzival.pagepulse.properties.DatabaseProperties;
import space.parzival.pagepulse.properties.SocialProperties;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;
import space.parzival.pagepulse.utils.TestUtils;

public class ApiControllerTest {

  private ApiController apiController;
  private SocialProperties socials;
  private DatabaseManager database;
  
  public ApiControllerTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, SQLException {
    this.apiController = new ApiController();

    // populate configurations
    List<ServiceConfiguration> fConfigurations = new ArrayList<>();
    fConfigurations.add(TestUtils.createFakeServiceConfiguration());

    ApplicationProperties appProperties = new ApplicationProperties();
    DatabaseProperties dbProperties = new DatabaseProperties();
    dbProperties.setConnection("jdbc:sqlite::memory:"); // run a fake in-memory database
    appProperties.setServices(fConfigurations);

    this.database = new DatabaseManager(dbProperties, appProperties);
    this.socials = new SocialProperties();
    this.socials.setDiscord("discord");
    this.socials.setGithub("github");

    // access private fields and inject values
    Field databaseField = this.apiController.getClass().getDeclaredField("database");
    Field socialField = this.apiController.getClass().getDeclaredField("social");
    databaseField.setAccessible(true);
    socialField.setAccessible(true);
    databaseField.set(this.apiController, this.database);
    socialField.set(this.apiController, this.socials);
  }

  /**
   * Checks the /api/services endpoint.
   */
  @Test
  void servicesTest() {
    int expectedServices = this.database.getServices().size();

    // check services endpoint
    List<Service> services = this.apiController.services();
    
    assertEquals(expectedServices, services.size());
  }

  /**
   * Checks the /api/history endpoint.
   */
  @Test
  void historyTest() {
    int expectedEntries = this.database.getHistory(1, 10).size();

    // check history endpoint
    List<HistoryEntry> historyEntries = this.apiController.history(1, 10);

    assertEquals(expectedEntries, historyEntries.size());
  }

  /**
   * Checks the /api/socials endpoint.
   */
  @Test
  void socialsTest() {
    // get socials from api
    SocialProperties apiSocials = this.apiController.socials();

    assertEquals(this.socials, apiSocials);
  }
}
