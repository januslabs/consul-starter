package org.januslabs.consul.kv.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class BaseConfig implements IConfig {

  private @JsonIgnore List<String> apps;

  public @JsonProperty("apps") List<String> getCleanAppsList() {
    return apps.parallelStream().distinct().collect(Collectors.toList());
  }

  public String toJson() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
  }

  public IConfig fromJson(String jsonInString) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonInString, DatasourceConfig.class);
  }
}
