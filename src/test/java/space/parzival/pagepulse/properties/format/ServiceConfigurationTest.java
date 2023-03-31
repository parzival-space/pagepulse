package space.parzival.pagepulse.properties.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ServiceConfigurationTest {
  
  @Test
  void serviceConfigurationTest() {
    ServiceConfiguration configuration = new ServiceConfiguration();

    // set values to something and check result
    configuration.setEndpointHidden(true);
    assertTrue(configuration.isEndpointHidden());

    configuration.setInterval(1234);
    assertEquals(1234, configuration.getInterval());
  }
}
