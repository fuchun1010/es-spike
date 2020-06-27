package com.tank.util;

import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.NonNull;

/**
 * @author tank198435163.com
 */
public class ExecutablePrivateUtil {

  @SuppressWarnings("unchecked")
  public <I, R> Try<R> invokeWithResult(@NonNull final Class<I> clazz, @NonNull final String methodName, Object... parameters) {

    return Stream.of(clazz.getDeclaredMethods())
            .filter(method -> methodName.equalsIgnoreCase(method.getName()))
            .map(method -> Try.of(() -> (R) method.invoke(clazz.newInstance(), parameters)))
            .getOrElseThrow(() -> new IllegalArgumentException("参数个数不对"));

  }

}
