package space.parzival.pagepulse.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import space.parzival.pagepulse.DatabaseManager;
import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.database.Status;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private DatabaseManager database;
  
  @GetMapping("/test")
  public String index() {
    this.database.addHistoryEntry(1, new Timestamp(new Date().getTime()), Status.OPERATIONAL, null);
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
