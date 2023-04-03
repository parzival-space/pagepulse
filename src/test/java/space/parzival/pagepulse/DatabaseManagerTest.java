package space.parzival.pagepulse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.properties.ApplicationProperties;
import space.parzival.pagepulse.properties.DatabaseProperties;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;
import space.parzival.pagepulse.utils.TestUtils;

class DatabaseManagerTest {
  
  private ApplicationProperties fApplicationProperties = new ApplicationProperties();
  private DatabaseProperties fDatabaseProperties = new DatabaseProperties();

  public DatabaseManagerTest() {
    // populate configurations
    List<ServiceConfiguration> fConfigurations = new ArrayList<>();

    // create a instance of a service
    fConfigurations.add(TestUtils.createFakeServiceConfiguration());
    fConfigurations.add(TestUtils.createFakeServiceConfiguration());
    fConfigurations.add(TestUtils.createFakeServiceConfiguration());
    
    this.fDatabaseProperties.setConnection("jdbc:sqlite::memory:"); // run a fake in-memory database
    this.fApplicationProperties.setServices(fConfigurations);
  }

  /**
   * Check if connecting to a database is possible.
   */
  @Test
  void connectDatabaseTest() {
    // connect to db
    DatabaseManager dbManager = assertDoesNotThrow(() -> new DatabaseManager(this.fDatabaseProperties, this.fApplicationProperties));

    // check if database matches the the number of fake services
    assertEquals(this.fApplicationProperties.getServices().size(), dbManager.getServices().size());
  }

  /**
   * Check if fetching the service history works.
   */
  @Test
  void serviceHistoryTest() {
    // connect to db
    DatabaseManager dbManager = assertDoesNotThrow(() -> new DatabaseManager(this.fDatabaseProperties, this.fApplicationProperties));

    // create fake history entry
    HistoryEntry fHistoryEntry = TestUtils.createFakeHistoryEntry();

    // add a history entry for first service
    dbManager.addHistoryEntry(1, fHistoryEntry.getTimestamp(), fHistoryEntry.getStatus(), fHistoryEntry.getError(), fHistoryEntry.getPossibleCause());

    // check if history entry was added
    List<HistoryEntry> historyEntries = dbManager.getHistory(1, 3);
    
    assertEquals(fHistoryEntry.getStatus(), historyEntries.get(0).getStatus());
    assertEquals(fHistoryEntry.getError(), historyEntries.get(0).getError());
    assertEquals(fHistoryEntry.getPossibleCause(), historyEntries.get(0).getPossibleCause());
  }

  /**
   * Test the removing of old entries from the service history.
   */
  @Test
  void historyCleanupTest() {
    // connect to db
    DatabaseManager dbManager = assertDoesNotThrow(() -> new DatabaseManager(this.fDatabaseProperties, this.fApplicationProperties));

    // fill history with trash
    int fillUntil = 15;
    for (int i = 0; i < fillUntil; i++) {
      HistoryEntry entry = TestUtils.createFakeHistoryEntry();
      dbManager.addHistoryEntry(1, entry.getTimestamp(), entry.getStatus(), entry.getError(), entry.getPossibleCause());
    }
    assertEquals(fillUntil, dbManager.getHistory(1, 20).size());

    // cleanup
    int expectedAfterCleanup = 5;
    dbManager.cleanupOldEntries(1, expectedAfterCleanup);
    assertEquals(expectedAfterCleanup, dbManager.getHistory(1, 20).size());
  }

  /**
   * Check if the application can fallback to a in-memory database when no database is provided.
   */
  @Test
  void missingConnectionTest() {
    // connect to db
    assertDoesNotThrow(() -> new DatabaseManager(new DatabaseProperties(), this.fApplicationProperties));
  }

  /**
   * Check if the application is able to detect that there are no services provided by the user.
   */
  @Test
  void missingServicesTest() {
    ApplicationProperties emptyProperties = new ApplicationProperties();

    assertDoesNotThrow(() -> new DatabaseManager(this.fDatabaseProperties, emptyProperties));
  }
}
