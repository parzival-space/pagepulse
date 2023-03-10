package space.parzival.pagepulse.properties.format;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HistoryEntry {
  private Timestamp timestamp;
  private Status status;
  private String error = null;

  public void parseStatus(String status) {
    switch (status.toUpperCase()) {
      case "OPERATIONAL":
        this.status = Status.OPERATIONAL;
        break;

      case "LIMITED":
        this.status = Status.LIMITED;
        break;

      case "OFFLINE":
        this.status = Status.OFFLINE;
        break;
    
      default:
        this.status = Status.UNKNOWN;
        break;
    }
  }
}
