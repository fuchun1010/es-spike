package com.tank.protocol.condition;

import java.beans.Transient;

/**
 * @author tank198435163.com
 */

public interface NestCondition {

  /**
   * 是否是符合条件
   *
   * @return
   */
  @Transient
  boolean isComplex();
}
