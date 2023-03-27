package space.parzival.pagepulse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.Scope;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import space.parzival.pagepulse.database.Service;
import space.parzival.pagepulse.database.Status;
import space.parzival.pagepulse.properties.ApplicationProperties;
import space.parzival.pagepulse.properties.format.ServiceConfiguration;

@Slf4j
@Component
public class HealthChecker implements InitializingBean {
  private Timer checkInterval = new Timer();

  @Autowired
  private DatabaseManager database;

  @Autowired
  private ApplicationProperties properties;

  @Override
  public void afterPropertiesSet() throws Exception {
    // configure ping intervals for services
    for (Service sConf : database.getServices()) {
      ServiceConfiguration serviceConfiguration = properties.getServices().stream()
        .filter(service -> 
          sConf.getName().equals(service.getName()) &&
          sConf.getGroup().equals(service.getGroup()) &&
          sConf.getEndpoint().equals(service.getEndpoint())
        )
        .findAny()
        .orElse(null);

      // get interval
      long interval = serviceConfiguration != null ? serviceConfiguration.getCheckInterval() : 5;

      this.checkInterval.scheduleAtFixedRate((
        new TimerTask() {

          @Override
          public void run() {
            Status status = Status.UNKNOWN;
            String error = null;

            try {
              // check connection
              URL endpoint = sConf.getEndpoint().toURL();
              HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

              // check ssl
              if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;

                // check certificate
                httpsConnection.connect();
                for (Certificate cert : httpsConnection.getServerCertificates()) {
                  ((X509Certificate) cert).checkValidity();
                }
              }

              // all checks passed
              status = Status.OPERATIONAL;
            }
            catch (SSLHandshakeException sslhe) {
              log.info("SSL connection failed ({}/{}): {}", sConf.getGroup(), sConf.getName(), sslhe.getMessage());
              
              status = Status.LIMITED;
              error = sslhe.getMessage();
            }
            catch (CertificateException ce) {
              log.info("Certificate check ({}/{}): {}", sConf.getGroup(), sConf.getName(), ce.getMessage());
              
              status = Status.LIMITED;
              error = ce.getMessage();
            }
            catch (IOException ioe) {
              log.info("Connection check failed ({}/{}): {}", sConf.getGroup(), sConf.getName(), ioe.getMessage());

              ioe.printStackTrace();

              status = Status.OFFLINE;
              error = "Failed to connect to server.";
            }

            database.addHistoryEntry(sConf.getId(), new Timestamp(new Date().getTime()), status, error);
          }
          
        }),
        0,
        interval * 1000 * 60
      );
    }
  }
  
}
