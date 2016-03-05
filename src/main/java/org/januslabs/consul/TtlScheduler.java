package org.januslabs.consul;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;

import com.orbitz.consul.Consul;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.Registration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TtlScheduler {

  public static final DateTime EXPIRED_DATE = new DateTime(0);
  private final Map<String, DateTime> serviceHeartbeats = new ConcurrentHashMap<>();

  public HeartbeatProperties configuration;

  public Consul client;

  public TtlScheduler(HeartbeatProperties configuration, Consul client) {
    this.configuration = configuration;
    this.client = client;
  }

  /**
   * Add a service to the checks loop.
   */
  public void add(final Registration service) {
    log.info("Adding Service to Scheduled" + service.toString());
    serviceHeartbeats.put(service.getId(), EXPIRED_DATE);
  }

  public void remove(String serviceId) {
    serviceHeartbeats.remove(serviceId);
  }

  @Scheduled(initialDelay = 0, fixedRateString = "15000")
  protected void heartbeatServices() throws NotRegisteredException {
    for (String serviceId : serviceHeartbeats.keySet()) {
      DateTime latestHeartbeatDoneForService = serviceHeartbeats.get(serviceId);
      if (latestHeartbeatDoneForService.plus(configuration.getHeartbeatInterval())
          .isBefore(DateTime.now())) {
        /*
         * String checkId = serviceId; if (!checkId.startsWith("service:")) { checkId = "service:" +
         * checkId; }
         */
        client.agentClient().pass(serviceId + ":2");
        log.debug("Sending consul heartbeat for: " + serviceId);
        serviceHeartbeats.put(serviceId, DateTime.now());
      }
    }
  }
}
