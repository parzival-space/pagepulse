package space.parzival.pagepulse.properties.format;

import java.net.URI;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServiceConfiguration {
  private String name;
  private String group;
  private URI endpoint;
  private int checkInterval = 5;
}
