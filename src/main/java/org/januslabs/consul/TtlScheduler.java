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
    log.info("Adding Service to Scheduled {}", service);
    serviceHeartbeats.put(service.getId(), EXPIRED_DATE);
  }

  public void remove(String serviceId) {
    serviceHeartbeats.remove(serviceId);
  }

  @Scheduled(initialDelay = 0, fixedRateString = "20000")
  protected void heartbeatServices() throws NotRegisteredException {
    for (String serviceId : serviceHeartbeats.keySet()) {
      DateTime latestHeartbeatDoneForService = serviceHeartbeats.get(serviceId);
      if (latestHeartbeatDoneForService.plus(configuration.getHeartbeatInterval())
          .isBefore(DateTime.now())) {

        log.info("Service ID check {}", serviceId);

        client.agentClient().pass(serviceId + ":2");

        /*
         * Map<String, HealthCheck> healthMaps = client.agentClient().getChecks();
         * healthMaps.entrySet().stream() .filter(healthcheck -> client.agentClient()
         * .isRegistered(healthcheck.getValue().getCheckId())) .forEach(rethrow(entry ->
         * client.agentClient().pass(entry.getValue().getCheckId())));
         * healthMaps.entrySet().stream() .filter(healthcheck -> client.agentClient()
         * .isRegistered(healthcheck.getValue().getCheckId())) .distinct().map(Map.Entry::getValue)
         * .forEach(rethrow(entry -> client.agentClient().pass(entry.getCheckId())));
         */

        log.info("Sending consul heartbeat for: " + serviceId);
        serviceHeartbeats.put(serviceId, DateTime.now());
      }
    }
  }


}
