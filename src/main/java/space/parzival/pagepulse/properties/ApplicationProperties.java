package space.parzival.pagepulse.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import space.parzival.pagepulse.properties.format.Service;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "pagepulse")
public class ApplicationProperties {
  
  private String demo;

  private List<Service> services;
}
