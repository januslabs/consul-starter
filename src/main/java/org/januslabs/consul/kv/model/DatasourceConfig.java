package org.januslabs.consul.kv.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DatasourceConfig extends BaseConfig {

  private String url;
  private String username;
  private String password;
  private String serviceName;


}
