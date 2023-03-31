package space.parzival.pagepulse.properties.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ServiceConfigurationTest {
  
  @Test
  public void serviceConfigurationTest() {
    ServiceConfiguration configuration = new ServiceConfiguration();

    // check if default value is set
    assertNotNull(configuration.getInterval());
    assertNotNull(configuration.isEndpointHidden());

    // set values to something and check result
    configuration.setEndpointHidden(true);
    assertTrue(configuration.isEndpointHidden());

    configuration.setInterval(1234);
    assertEquals(configuration.getInterval(), 1234);
  }
}
