package space.parzival.pagepulse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import space.parzival.pagepulse.properties.ApplicationProperties;
import space.parzival.pagepulse.properties.DatabaseProperties;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;
import space.parzival.pagepulse.utils.TestUtils;

class HealthCheckerTest {
  
  private DatabaseManager database;
  private ApplicationProperties properties;
  private DatabaseProperties dbProperties;

  private HealthChecker healthChecker;

  public HealthCheckerTest() throws SQLException, NoSuchFieldException, SecurityException {
    
    // create a instance of a service
    ServiceConfiguration fService = TestUtils.createFakeServiceConfiguration();

    // populate configurations
    List<ServiceConfiguration> fConfigurations = new ArrayList<>();
    fConfigurations.add(fService);

    this.properties = new ApplicationProperties();
    this.dbProperties = new DatabaseProperties();
    
    this.dbProperties.setConnection("jdbc:sqlite::memory:"); // run a fake in-memory database
    this.properties.setServices(fConfigurations);

    this.database = new DatabaseManager(dbProperties, properties);

    // create healtchecker intance
    this.healthChecker = new HealthChecker();
  }

  @Test
  public void afterPropertiesSetTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    // simulate autowire mechanic
    Field databaseField = this.healthChecker.getClass().getDeclaredField("database");
    Field propertiesField = this.healthChecker.getClass().getDeclaredField("properties");
    Field dbPropertiesField = this.healthChecker.getClass().getDeclaredField("dbProperties");
    databaseField.setAccessible(true);
    propertiesField.setAccessible(true);
    dbPropertiesField.setAccessible(true);
    
    databaseField.set(this.healthChecker, this.database);
    propertiesField.set(this.healthChecker, this.properties);
    dbPropertiesField.set(this.healthChecker, this.dbProperties);

    // there has to be a better way than accessing the fields using reflections...
    assertDoesNotThrow(() -> this.healthChecker.afterPropertiesSet());
  }
}
