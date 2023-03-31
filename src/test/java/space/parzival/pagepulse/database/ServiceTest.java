package space.parzival.pagepulse.database;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ServiceTest {
  
  @Test
  public void serviceTest() {
    Service service = new Service();

    // set values to something and check result
    service.setEndpointHidden(true);
    assertTrue(service.isEndpointHidden());
  }
}
