package space.parzival.pagepulse.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "pagepulse.social")
public class SocialProperties {
  /**
   * The name of your GitHub account or a repository.
   * This app will prepend 'https://github.com/' to whatever value you provide.
   * Disabled if not specified.
   */
  private String github = null;

  /**
   * The id of your Discord user.
   * This app will generate a direct link to your Discord account.
   * Disabled if not specified.
   */
  private String discord = null;
}
