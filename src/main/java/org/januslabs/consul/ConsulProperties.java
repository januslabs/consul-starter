package org.januslabs.consul;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("consul")
@Data
public class ConsulProperties {

  @NotNull
  private String host = "localhost";

  @NotNull
  private int port = 8500;

  @NotNull
  private String url = "http://localhost:8500";

  private List<String> tags = new ArrayList<>();

  private boolean enabled = true;

  private String prefix = "config";

  private List<String> managementTags = Arrays.asList("management");
}
