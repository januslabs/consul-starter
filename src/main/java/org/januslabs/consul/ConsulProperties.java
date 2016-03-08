package org.januslabs.consul;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@ConfigurationProperties("consul")
@Data
public class ConsulProperties {


  private String host = "localhost";
  private int port = 8500;
  private String url = "http://localhost:8500";
  private List<String> tags = new ArrayList<>();
  private boolean enabled = true;
  private String prefix = "config";
  private List<String> managementTags = Arrays.asList("management");
}
