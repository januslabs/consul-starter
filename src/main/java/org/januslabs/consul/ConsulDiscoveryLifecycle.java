package org.januslabs.consul;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.januslabs.consul.tomcat.TomcatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.AbstractDiscoveryLifecycle;

import com.google.common.collect.ImmutableList;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsulDiscoveryLifecycle extends AbstractDiscoveryLifecycle {

  @Autowired
  public Consul client;

  @Autowired
  public ConsulProperties consulProperties;


  @Autowired
  public TtlScheduler ttlScheduler;

  @Autowired
  public HeartbeatProperties ttlConfig;

  @Override
  protected void register() {

    String appName = getAppName();
    String address = "localhost";

    try {
      address = InetAddress.getLocalHost().getHostAddress();

    } catch (UnknownHostException e) {
      address = "localhost";
    }
    Integer port;
    try {
      port = new TomcatUtils().getContainerPort();
    } catch (Exception e) {

      e.printStackTrace();
      port = new Integer(getEnvironment().getProperty("server.port"));
    }

    String contextPath = getEnvironment().getProperty("server.context-path");
    ttlConfig.setPort(port);
    List<Registration.RegCheck> regChecks = ImmutableList.of(
        Registration.RegCheck.http(ttlConfig.getHttpUrl() + contextPath + "/health", 30),
        Registration.RegCheck.ttl(Long.valueOf(ttlConfig.getTtl())));

    Registration service = ImmutableRegistration.builder().name(appName).id(getContext().getId())
        .address(address).port(port).checks(regChecks).tags(consulProperties.getTags())

        .build();
    register(service);

  }

  @Override
  protected void registerManagement() {
    String address = "localhost";

    try {
      address = InetAddress.getLocalHost().getHostAddress();

    } catch (UnknownHostException e) {
      address = "localhost";
    }

    Integer port;
    try {
      port = new TomcatUtils().getContainerPort();
    } catch (Exception e) {

      e.printStackTrace();
      port = new Integer(getEnvironment().getProperty("management.port"));
    }

    Registration management = ImmutableRegistration.builder().id(getManagementServiceId())
        .address(address).name(getManagementServiceName()).port(port)
        .tags(consulProperties.getManagementTags()).build();

    register(management);
  }

  protected void register(Registration service) {
    log.info("Registering service with consul: {}", service.toString());
    client.agentClient().register(service);
    try {
      client.agentClient().pass(service.getId());
    } catch (Exception e) {

    }
    ttlScheduler.add(service);
  }

  @Override
  protected Object getConfiguration() {
    return consulProperties;
  }


  @Override
  protected void deregister() {
    deregister(getContext().getId());
  }

  @Override
  protected void deregisterManagement() {
    if (shouldRegisterManagement()) {
      deregister(getManagementServiceName());
    }
  }

  private void deregister(String serviceId) {
    ttlScheduler.remove(serviceId);
    client.agentClient().deregister(serviceId);
  }

  @Override
  protected boolean isEnabled() {
    return true;
  }

  @Override
  protected int getConfiguredPort() {

    return new Integer(getEnvironment().getProperty("server.port", "8080"));
  }

  @Override
  protected void setConfiguredPort(int port) {


  }


}
