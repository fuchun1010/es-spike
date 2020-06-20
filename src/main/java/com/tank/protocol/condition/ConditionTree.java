package com.tank.protocol.condition;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.beans.Transient;
import java.util.List;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
public class ConditionTree implements NestCondition {

  @Transient
  public void addCondition(@NonNull final NestCondition condition) {
    this.conditions.add(condition);
  }

  @Override
  @Transient
  public boolean isComplex() {
    return false;
  }

  private String logical;

  private List<NestCondition> conditions = Lists.newArrayList();


}
