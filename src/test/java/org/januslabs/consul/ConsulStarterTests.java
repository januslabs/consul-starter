package org.januslabs.consul;

import org.januslabs.consul.ConsulStarterTests.ConsulApplication;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.orbitz.consul.Consul;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ConsulApplication.class)
@WebIntegrationTest(randomPort=true)

public class ConsulStarterTests {


  @Value("${local.server.port}")
  private int port;

  @Configuration
  @EnableWebMvc
  @Import({EmbeddedServletContainerAutoConfiguration.class,
      DispatcherServletAutoConfiguration.class, ServerPropertiesAutoConfiguration.class,
      HttpMessageConvertersAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class})
  @ComponentScan("org.januslabs.consul")
  public static class ConsulApplication {

    public static void main(String[] args) throws Exception {
      SpringApplication.run(ConsulAutoConfiguration.class, args);
    }

  }

  private @Autowired Consul consulClient;


  @Test
  public void consul() {
    Assert.assertNotNull(consulClient);

  }
  
  @After
  public void tearDown()
  {
    consulClient.agentClient().deregister("service:consul-starter-test-service");
    
  }
}
