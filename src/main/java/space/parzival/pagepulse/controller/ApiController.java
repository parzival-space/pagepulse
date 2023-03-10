package space.parzival.pagepulse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import space.parzival.pagepulse.DatabaseManager;
import space.parzival.pagepulse.properties.format.HistoryEntry;
import space.parzival.pagepulse.properties.format.Service;
import space.parzival.pagepulse.properties.format.Status;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private DatabaseManager database;
  
  @GetMapping("/test")
  public String index() {
    this.database.addHistoryEntry(1, Status.OPERATIONAL);
    return "OK";
  }

  @GetMapping("/services")
  public List<Service> services() {
    return this.database.getServices();
  }

  @GetMapping("/history")
  public List<HistoryEntry> history(int serviceId) {
    return this.database.getHistory(serviceId);
  }
}
