package space.parzival.pagepulse.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import space.parzival.pagepulse.DatabaseManager;
import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.properties.SocialProperties;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private DatabaseManager database;

  @Autowired
  private SocialProperties social;

  @GetMapping("/services")
  public List<Service> services() {
    List<Service> services = this.database.getServices();

    // hide urls for wanted entries
    services.forEach(s -> {
      if (!s.isEndpointHidden()) return;

      s.setEndpoint(null);
    });

    return services;
  }

  @GetMapping("/history")
  public List<HistoryEntry> history(Integer serviceId, Integer limit) {
    if (serviceId == null) return new ArrayList<>();
    if (limit == null) limit = 10;

    return this.database.getHistory(serviceId, limit);
  }

  @GetMapping("/socials")
  public SocialProperties socials() {
    return this.social;
  }
}
