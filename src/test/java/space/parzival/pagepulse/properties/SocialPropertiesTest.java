package space.parzival.pagepulse.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class SocialPropertiesTest {
  
  @Test
  void socialPropertiesTest() {
    SocialProperties socialProperties = new SocialProperties();

    // verify fallback values
    assertNull(socialProperties.getDiscord());
    assertNull(socialProperties.getGithub());

    // set to something
    socialProperties.setDiscord("discord");
    socialProperties.setGithub("github");

    // verify
    assertEquals("discord", socialProperties.getDiscord());
    assertEquals("github", socialProperties.getGithub());
  }
}
