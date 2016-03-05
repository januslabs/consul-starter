package org.januslabs.consul.kv.model;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class DatasourceConfigBuilder {

  @FunctionalInterface
  public interface DatasourceConfigSetter extends Consumer<DatasourceConfig> {
  }

  public static DatasourceConfig build(DatasourceConfigSetter... datasourceSetters) {
    final DatasourceConfig datasource = new DatasourceConfig();

    Stream.of(datasourceSetters).forEach(s -> s.accept(datasource));

    return datasource;
  }
}
