package space.parzival.pagepulse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.properties.ApplicationProperties;
import space.parzival.pagepulse.properties.DatabaseProperties;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;

class HealthCheckerTest {
  
  private DatabaseManager database;
  private ApplicationProperties properties;
  private DatabaseProperties dbProperties;

  private HealthChecker healthChecker;

  public HealthCheckerTest() throws SQLException, NoSuchFieldException, SecurityException {
    // create fake services
    ServiceConfiguration valid = new ServiceConfiguration();
    valid.setEndpoint(URI.create("https://example.com"));
    valid.setName("Valid");
    valid.setGroup("Test");

    ServiceConfiguration sslError = new ServiceConfiguration();
    sslError.setEndpoint(URI.create("https://revoked.badssl.com/"));
    sslError.setName("SSL Error");
    sslError.setGroup("Test");

    ServiceConfiguration invalid = new ServiceConfiguration();
    invalid.setEndpoint(URI.create("https://wrong.domain.name.abcdf"));
    invalid.setName("Invalid");
    invalid.setGroup("Test");


    // populate configurations
    List<ServiceConfiguration> fConfigurations = new ArrayList<>();
    fConfigurations.add(valid);
    fConfigurations.add(sslError);
    fConfigurations.add(invalid);

    this.properties = new ApplicationProperties();
    this.dbProperties = new DatabaseProperties();
    
    this.dbProperties.setConnection("jdbc:sqlite::memory:"); // run a fake in-memory database
    this.properties.setServices(fConfigurations);

    this.database = new DatabaseManager(dbProperties, properties);

    // create HealthChecker instance
    this.healthChecker = new HealthChecker();
  }

  @Test
  void afterPropertiesSetTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException {
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

  @Test
  void runServiceCheckTest() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
    // simulate autowire mechanic
    Field databaseField = this.healthChecker.getClass().getDeclaredField("database");
    Method runServiceCheck = this.healthChecker.getClass().getDeclaredMethod("runServiceCheck", Service.class);

    databaseField.setAccessible(true);
    runServiceCheck.setAccessible(true);
    
    databaseField.set(this.healthChecker, this.database);

    Service valid = this.database.getServices().get(0);
    Service sslError = this.database.getServices().get(1);
    Service invalid = this.database.getServices().get(2);

    // run test
    assertDoesNotThrow(() -> runServiceCheck.invoke(this.healthChecker, valid));
    assertDoesNotThrow(() -> runServiceCheck.invoke(this.healthChecker, sslError));
    assertDoesNotThrow(() -> runServiceCheck.invoke(this.healthChecker, invalid));

    assertEquals(1, this.database.getHistory(valid.getId(), 10).size());
    assertEquals(1, this.database.getHistory(sslError.getId(), 10).size());
    assertEquals(1, this.database.getHistory(invalid.getId(), 10).size());
  }
}
