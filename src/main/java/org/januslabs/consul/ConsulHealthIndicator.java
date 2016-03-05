package org.januslabs.consul;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import com.orbitz.consul.Consul;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.Agent;
import com.orbitz.consul.option.QueryOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsulHealthIndicator extends AbstractHealthIndicator {

  @Autowired
  public Consul consul;

  @Override
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    try {
      log.info("doHealthCheck.....");
      Agent agentSelf = consul.agentClient().getAgent();
      log.info(agentSelf.toString());
      log.info(agentSelf.getConfig().toString());
      ConsulResponse<Map<String, List<String>>> services =
          consul.catalogClient().getServices(QueryOptions.BLANK);
      builder.up().withDetail("services", services.getResponse())
          .withDetail("advertiseAddress", agentSelf.getConfig().getAdvertiseAddr())
          .withDetail("datacenter", agentSelf.getConfig().getDatacenter())
          .withDetail("domain", agentSelf.getConfig().getDomain())
          .withDetail("nodeName", agentSelf.getConfig().getNodeName())
          .withDetail("bindAddress", agentSelf.getConfig().getBindAddr())
          .withDetail("clientAddress", agentSelf.getConfig().getClientAddr());


    } catch (Exception e) {
      builder.down(e);
    }
  }

}
