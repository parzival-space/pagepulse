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

  /**
   * Tries to connect to a in-memory database.
   * This will setup the default tables and fill them with the configured service configuraitons.
   */
  public DatabaseManagerTest() {
    // create a instance of a service
    ServiceConfiguration fService = TestUtils.createFakeServiceConfiguration();

    // populate configurations
    List<ServiceConfiguration> fConfigurations = new ArrayList<>();
    fConfigurations.add(fService);
    
    this.fDatabaseProperties.setConnection("jdbc:sqlite::memory:"); // run a fake in-memory database
    this.fApplicationProperties.setServices(fConfigurations);
  }

  @Test
  public void connectDatabaseTest() {
    // connect to db
    DatabaseManager dbManager = assertDoesNotThrow(() -> new DatabaseManager(this.fDatabaseProperties, this.fApplicationProperties));

    // check if database matches the the number of fake services
    assertEquals(this.fApplicationProperties.getServices().size(), dbManager.getServices().size());
  }

  @Test
  public void serviceHistoryTest() {
    // connect to db
    DatabaseManager dbManager = assertDoesNotThrow(() -> new DatabaseManager(this.fDatabaseProperties, this.fApplicationProperties));

    // create fake history entry
    HistoryEntry fHistoryEntry = TestUtils.createFakeHistoryEntry();

    // add a history entry for first service
    dbManager.addHistoryEntry(1, fHistoryEntry.getTimestamp(), fHistoryEntry.getStatus(), fHistoryEntry.getError(), fHistoryEntry.getPossibleCause());

    // check if history entry was added
    List<HistoryEntry> historyEntries = dbManager.getHistory(1, 3);
    
    assertEquals(fHistoryEntry.getStatus(), historyEntries.get(0).getStatus());
    assertEquals(fHistoryEntry.getTimestamp(), historyEntries.get(0).getTimestamp());
    assertEquals(fHistoryEntry.getError(), historyEntries.get(0).getError());
    assertEquals(fHistoryEntry.getPossibleCause(), historyEntries.get(0).getPossibleCause());
  }

  @Test
  public void historyCleanupTest() {
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
}
