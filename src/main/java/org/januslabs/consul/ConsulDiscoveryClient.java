package org.januslabs.consul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.orbitz.consul.Consul;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.Agent;
import com.orbitz.consul.model.agent.Member;
import com.orbitz.consul.model.catalog.CatalogService;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.option.QueryOptions;

public class ConsulDiscoveryClient implements DiscoveryClient, ApplicationContextAware {

  @Autowired
  ApplicationContext context;

  @Autowired
  public Consul client;

  @Override
  public String description() {
    return "Consul  discovery client";
  }

  @Override
  public List<ServiceInstance> getInstances(String serviceId) {
    List<ServiceInstance> instances = new ArrayList<>();

    addInstancesToList(instances, serviceId);

    return instances;
  }

  @Override
  public ServiceInstance getLocalServiceInstance() {
    Map<String, Service> servicesMap = client.agentClient().getServices();
    Service service = servicesMap.get(context.getId());
    if (service == null) {
      throw new IllegalStateException(
          "Unable to locate service in consul agent: " + context.getId());
    }
    String host = "localhost";
    Agent agentSelf = client.agentClient().getAgent();
    Member member = agentSelf.getMember();
    if (member != null) {
      if (member.getName() != null) {
        host = member.getName();
      }
    }
    return new DefaultServiceInstance(service.getId(), host, service.getPort(), false);
  }

  @Override
  public List<String> getServices() {

    return new ArrayList<String>(
        client.catalogClient().getServices(QueryOptions.BLANK).getResponse().keySet());
  }

  public List<ServiceInstance> getAllInstances() {
    List<ServiceInstance> instances = new ArrayList<>();

    ConsulResponse<Map<String, List<String>>> services =
        client.catalogClient().getServices(QueryOptions.BLANK);
    for (String serviceId : services.getResponse().keySet()) {
      addInstancesToList(instances, serviceId);
    }
    return instances;
  }

  private void addInstancesToList(List<ServiceInstance> instances, String serviceId) {
    ConsulResponse<List<CatalogService>> services =
        client.catalogClient().getService(serviceId, QueryOptions.BLANK);
    for (CatalogService service : services.getResponse()) {
      instances.add(new DefaultServiceInstance(serviceId, service.getNode(),
          service.getServicePort(), false));
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    this.context = context;

  }

}
