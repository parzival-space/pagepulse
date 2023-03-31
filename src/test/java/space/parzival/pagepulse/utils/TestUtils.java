package space.parzival.pagepulse.utils;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.database.Status;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;

public class TestUtils {
  /**
   * Creates a new HistoryEntry instance with a random name and a random group.
   */
  public static HistoryEntry createFakeHistoryEntry() {
    HistoryEntry fHistoryEntry = new HistoryEntry();
    fHistoryEntry.setError(UUID.randomUUID().toString());
    fHistoryEntry.setPossibleCause(UUID.randomUUID().toString());
    fHistoryEntry.setStatus(Status.OFFLINE);
    fHistoryEntry.setTimestamp(new Timestamp(new Date().getTime()));
    return fHistoryEntry;
  }

  /**
   * Creates a new ServiceConfiguration with a random name and a random group.
   * @return Randomized ServiceConfiguration instance.
   */
  public static ServiceConfiguration createFakeServiceConfiguration() {
    ServiceConfiguration fService = new ServiceConfiguration();
    fService.setName(UUID.randomUUID().toString());
    fService.setGroup(UUID.randomUUID().toString());
    fService.setEndpoint(URI.create("https://example.com"));
    fService.setEndpointHidden(true);
    fService.setInterval(15);
    return fService;
  }
}
