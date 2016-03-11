package org.januslabs.consul;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.joda.time.Period;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ConfigurationProperties(prefix = "consul.heartbeat")
@Data
@Slf4j
public class HeartbeatProperties {

  @Min(1)
  private int ttlValue = 30;

  @NotNull
  private String ttlUnit = "";

  @NotNull
  private String ttlUrl = "";

  @NotNull
  private int port = 0;

  @DecimalMin("0.1")
  @DecimalMax("0.9")
  private double intervalRatio = 2.0 / 3.0;

  private Period heartbeatInterval;

  @PostConstruct
  public void init() {
    this.heartbeatInterval = computeHearbeatInterval();
    log.debug("Computed heartbeatInterval: " + heartbeatInterval);
  }

  /*
   * Every 20 seconds
   */
  protected Period computeHearbeatInterval() {

    double interval = ttlValue * intervalRatio;
    double max = Math.max(interval, 1);
    int ttlMinus1 = ttlValue - 1;
    double min = Math.min(ttlMinus1, max);
    long periodValue = Math.round(1000 * min);
    return new Period(periodValue);
  }

  public String getTtl() {
    return ttlValue + ttlUnit;
  }

  public String getHttpUrl() {
    try {
      ttlUrl = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port;
    } catch (UnknownHostException e) {

    }
    return ttlUrl;
  }
}
