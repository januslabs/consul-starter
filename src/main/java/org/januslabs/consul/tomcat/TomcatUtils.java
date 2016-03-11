package org.januslabs.consul.tomcat;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11AprProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.coyote.http11.Http11Protocol;
import org.springframework.stereotype.Component;

@Component
public class TomcatUtils {

  public Integer getContainerPort() throws Exception {
    Integer port = 0;
    MBeanServer mbeanserver = MBeanServerFactory.findMBeanServer(null).get(0);
    Server server = (Server) mbeanserver.getAttribute(new ObjectName("Catalina", "type", "Server"),
        "managedResource");
    Service[] services = server.findServices();

    for (Service service : services) {
      for (Connector connector : service.findConnectors()) {
        ProtocolHandler protocolhandler = connector.getProtocolHandler();
        if (protocolhandler instanceof Http11AprProtocol
            || protocolhandler instanceof Http11NioProtocol
            || protocolhandler instanceof Http11Protocol) {
          port = connector.getPort();
          break;
        }
      }
    }
    return port;
  }
}
