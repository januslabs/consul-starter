package org.januslabs.consul;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties
@EnableScheduling
@ComponentScan(value = "org.januslabs.consul")
@Slf4j
public class ConsulAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ConsulProperties consulProperties() {
    return new ConsulProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  public Consul consulClient(ConsulProperties consulProperties) {
    log.info("Connecting to consul server {}, {}, {}", consulProperties.getHost(),
        consulProperties.getUrl(), consulProperties.getPort());
    if (consulProperties.getUrl() != null) {
      return Consul.builder().withUrl(consulProperties.getUrl()).build();
    } else {
      HostAndPort hostAndPort =
          HostAndPort.fromParts(consulProperties.getHost(), consulProperties.getPort());
      return Consul.builder().withHostAndPort(hostAndPort).build();
    }

  }

  @Bean
  public ConsulHealthIndicator consulHealthIndicator() {
    return new ConsulHealthIndicator();
  }
}
