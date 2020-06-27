package com.tank.util;

import lombok.NonNull;

import java.util.Objects;

/**
 * @author tank198435163.com
 */
public class StringWrapper {

  /**
   * sub string from start position
   *
   * @param str
   * @param start
   * @return
   */
  public String subString(@NonNull final String str, @NonNull final Integer start) {
    return Objects.isNull(start) ? str : str.substring(start);
  }

}
