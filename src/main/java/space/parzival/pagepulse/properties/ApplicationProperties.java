package space.parzival.pagepulse.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "pagepulse")
public class ApplicationProperties {
  /**
   * A list of services that will be displayed and scanned by this app.
   */
  private List<ServiceConfiguration> services = new ArrayList<>();
}
