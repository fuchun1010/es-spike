package com.tank.protocol.condition;

import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.util.List;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
public class Condition<T> implements NestCondition {
  
  private String op;

  private String fieldName;

  private String type;

  private List<T> values;

  @Override
  @Transient
  public boolean isComplex() {
    return true;
  }
}
