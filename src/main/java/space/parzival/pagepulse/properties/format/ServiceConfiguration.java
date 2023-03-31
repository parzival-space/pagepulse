package space.parzival.pagepulse.properties.format;

import java.net.URI;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServiceConfiguration {
  /**
   * Display name of the service.
   * Example: Google
   */
  private String name;

  /**
   * The group to display this service in.
   * Example: Search Engines
   */
  private String group;

  /**
   * The URL endpoint to check.
   * Example: https://google.com
   */
  private URI endpoint;

  /**
   * The interval in minutes in which this endpoint gets called.
   */
  private int interval = 5;

  /**
   * Wether or not to display the endpoint in the UI.
   * If you have services that should not be accessible to everyone, you can hide them.
   */
  private boolean endpointHidden = false;
}
