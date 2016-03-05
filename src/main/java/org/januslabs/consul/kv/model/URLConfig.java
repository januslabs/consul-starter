package org.januslabs.consul.kv.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class URLConfig extends BaseConfig {

  private String url;
  private String serviceName;

}
