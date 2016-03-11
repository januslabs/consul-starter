package org.januslabs.consul;

import static org.januslabs.util.ExceptionUtil.rethrow;

import java.util.List;
import java.util.Map;

import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;

import com.orbitz.consul.Consul;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.health.HealthCheck;
import com.orbitz.consul.model.health.ServiceHealth;

public class ConsulClientTests {

  @Test
  public void computeHearbeatInterval() {

    double intervalRatio = 1.0 / 3.0;
    int ttlValue = 30;
    double interval = ttlValue * intervalRatio;
    double max = Math.max(interval, 1);
    int ttlMinus1 = ttlValue - 1;
    double min = Math.min(ttlMinus1, max);
    long periodValue = Math.round(1000 * min);
    Period p= new Period(periodValue);
    System.out.println(p.getSeconds());
    Assert.assertEquals(10, p.getSeconds());
  }
  
  @Test
  public void checkSerrviceID() throws Exception
  {
    String serviceId="consul-starter-tester:8080";
    Consul consul=Consul.builder().withUrl("http://localhost:8500").build();
    ConsulResponse<List<HealthCheck>> healthCheckResponse=consul.healthClient().getServiceChecks(serviceId);
    List<HealthCheck> healthChecks=healthCheckResponse.getResponse();
    healthChecks.forEach(i -> System.out.println(i));
    healthChecks.stream().filter(healthcheck -> consul.agentClient().isRegistered(serviceId)).forEach(rethrow(healthcheck -> consul.agentClient().pass(healthcheck.getCheckId())));
   
    Map<String, HealthCheck> healthMaps=consul.agentClient().getChecks();
    healthMaps.forEach((k,v) -> System.out.println("Key " + k + " value" + v));
    healthMaps.entrySet().stream().filter(healthcheck -> consul.agentClient().isRegistered(healthcheck.getValue().getCheckId())).forEach(rethrow(entry -> consul.agentClient().pass(entry.getValue().getCheckId())));
    //forEach(rethrowBiConsumer ((k,v) -> { consul.agentClient().pass(v.getCheckId()); }) );
    healthMaps.entrySet().stream().filter(healthcheck -> consul.agentClient().isRegistered(healthcheck.getValue().getCheckId())).distinct().map(Map.Entry::getValue).forEach(rethrow(entry -> consul.agentClient().pass(entry.getCheckId())));
    List<ServiceHealth> serviceHealths=consul.healthClient().getAllServiceInstances(serviceId).getResponse();
    serviceHealths.stream().forEach(servicehealth -> { servicehealth.getChecks().stream().forEach(check -> System.out.println(check)); } );
    
  }
}
