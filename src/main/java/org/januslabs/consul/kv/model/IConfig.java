package org.januslabs.consul.kv.model;

import java.util.List;

public interface IConfig {

  public List<String> getCleanAppsList();

  public String toJson() throws Exception;

  public IConfig fromJson(String jsonInString) throws Exception;

}
