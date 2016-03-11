package org.januslabs.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ExceptionUtil {

  @FunctionalInterface
  public interface Consumer_WithExceptions<T, E extends Exception> {
    void accept(T t) throws E;
  }

  @FunctionalInterface
  public interface BiConsumer_WithExceptions<T, U, E extends Exception> {
    void accept(T t, U u) throws E;
  }

  public static <T, E extends Exception> Consumer<T> rethrow(
      Consumer_WithExceptions<T, E> consumer) {
    return t -> {
      try {
        consumer.accept(t);
      } catch (Exception exception) {
        throwsAsUnchecked(exception);
      }
    };
  }


  public static <T, U, E extends Exception> BiConsumer<T, U> rethrowBiConsumer(
      BiConsumer_WithExceptions<T, U, E> consumer) {
    return (t, u) -> {
      try {
        consumer.accept(t, u);
      } catch (Exception exception) {
        throwsAsUnchecked(exception);
      }
    };
  }


  @SuppressWarnings("unchecked")
  private static <E extends Exception> void throwsAsUnchecked(Exception exception) throws E {
    throw (E) exception;

  }


}
