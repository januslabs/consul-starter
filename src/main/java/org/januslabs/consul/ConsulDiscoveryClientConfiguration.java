package org.januslabs.consul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orbitz.consul.Consul;

@Configuration
public class ConsulDiscoveryClientConfiguration {

  @Autowired
  public Consul consulClient;

  @Bean
  public ConsulDiscoveryLifecycle consulLifecycle() {
    return new ConsulDiscoveryLifecycle();
  }

  @Bean
  public TtlScheduler ttlScheduler() {
    return new TtlScheduler(heartbeatProperties(), consulClient);
  }

  @Bean
  public HeartbeatProperties heartbeatProperties() {
    return new HeartbeatProperties();
  }

  @Bean
  public ConsulDiscoveryClient consulDiscoveryClient() {
    return new ConsulDiscoveryClient();
  }
}
