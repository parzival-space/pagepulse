package space.parzival.pagepulse.database;

import java.net.URI;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Service {
  private int id;
  private String name;
  private String group;
  private URI endpoint;
  private boolean endpointHidden = false;
}
