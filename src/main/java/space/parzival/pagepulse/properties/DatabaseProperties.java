package space.parzival.pagepulse.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "pagepulse.database")
public class DatabaseProperties {
  private int queryTimeout = 30;
  private String connection = "";
  private String tablePrefix = "pagepulse_";

  private int cleanupInterval = 60;
  private int entryLimit = 10;
}
