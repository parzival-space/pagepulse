package space.parzival.pagepulse.database;

import java.net.URI;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Service {
  private int id;
  private String name;
  private String group;
  private URI endpoint;
}
