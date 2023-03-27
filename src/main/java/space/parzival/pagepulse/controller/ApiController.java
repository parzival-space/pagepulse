package space.parzival.pagepulse.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import space.parzival.pagepulse.DatabaseManager;
import space.parzival.pagepulse.database.HistoryEntry;
import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.database.Status;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private DatabaseManager database;
  
  @GetMapping("/test")
  public String index() {
    this.database.addHistoryEntry(1, new Timestamp(new Date().getTime()), Status.OPERATIONAL, null, null);
    return "OK";
  }

  @GetMapping("/services")
  public List<Service> services() {
    return this.database.getServices();
  }

  @GetMapping("/history")
  public List<HistoryEntry> history(Integer serviceId, Integer limit) {
    log.info("/history?serviceId={}&limit={}", serviceId, limit);

    if (serviceId == null) return new ArrayList<>();
    if (limit == null) limit = 10;

    return this.database.getHistory(serviceId, limit);
  }
}
