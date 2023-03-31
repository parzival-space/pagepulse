package space.parzival.pagepulse.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "pagepulse.database")
public class DatabaseProperties {

  /**
   * The maximum time in seconds until a SQL query fails.
   */
  private int queryTimeout = 30;

  /**
   * A JDBC connection string.
   * Example: jdbc:sqlite:./database.db
   */
  private String connection = "";

  /**
   * A prefix that will be prepended to every table created by this app.
   */
  private String tablePrefix = "pagepulse_";

  /**
   * The interval in seconds in which this app removed old entries from the history.
   */
  private int cleanupInterval = 60;

  /**
   * The maximum number of allowed entries in the history table.
   */
  private int entryLimit = 10;
}
