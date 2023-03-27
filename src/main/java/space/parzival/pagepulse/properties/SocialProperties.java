package space.parzival.pagepulse.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "pagepulse.social")
public class SocialProperties {
  private String github = null;
  private String discord = null;
}
