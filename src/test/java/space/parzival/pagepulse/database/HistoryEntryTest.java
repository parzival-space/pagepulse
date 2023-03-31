package space.parzival.pagepulse.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HistoryEntryTest {
  
  @Test
  public void parseStatusTest() {
    HistoryEntry historyEntry = new HistoryEntry();

    historyEntry.parseStatus("Operational");
    assertEquals(Status.OPERATIONAL, historyEntry.getStatus());

    historyEntry.parseStatus("Limited");
    assertEquals(Status.LIMITED, historyEntry.getStatus());

    historyEntry.parseStatus("Offline");
    assertEquals(Status.OFFLINE, historyEntry.getStatus());

    historyEntry.parseStatus("SomeRandomShit");
    assertEquals(Status.UNKNOWN, historyEntry.getStatus());
  }
}
