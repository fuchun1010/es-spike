package com.tank.util;

import io.vavr.collection.Stream;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.val;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author tank198435163.com
 */
public class BinarySearch {


  public <T extends Comparable<T>> Option<T> searchBy(@NonNull final T[] targets,
                                                      @NonNull final T data) {

    if (targets.length == 0) {
      throw new IllegalArgumentException("target length not allowed 0");
    }

    val startIndex = 0;
    val endIndex = targets.length - 1;

    if (startIndex == endIndex || endIndex == 1) {
      val result = Stream.of(targets).filter(d -> d.compareTo(data) == 0).filter(Objects::nonNull).getOrNull();
      return Objects.nonNull(result) ? Option.of(result) : Option.none();
    }

    val middleIndex = (int) Math.floor((double) (endIndex - startIndex) / 2);
    val middleData = targets[middleIndex];

    if (middleData.compareTo(data) == 0) {
      return Option.of(middleData);
    }

    T[] tmp = null;
    if (middleData.compareTo(data) < 0) {
      tmp = Arrays.copyOfRange(targets, middleIndex, endIndex + 1);
    } else {
      tmp = Arrays.copyOfRange(targets, 0, middleIndex + 1);
    }
    return this.searchBy(tmp, data);

  }

}
