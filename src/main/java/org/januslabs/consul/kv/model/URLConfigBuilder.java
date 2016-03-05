package org.januslabs.consul.kv.model;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class URLConfigBuilder {

  @FunctionalInterface
  public interface URLConfigSetter extends Consumer<URLConfig> {
  }

  public static URLConfig build(URLConfigSetter... urlSetters) {
    final URLConfig url = new URLConfig();

    Stream.of(urlSetters).forEach(s -> s.accept(url));

    return url;
  }
}
