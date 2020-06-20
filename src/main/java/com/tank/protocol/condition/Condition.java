package com.tank.protocol.condition;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.beans.Transient;
import java.util.List;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
@Accessors(chain = true)
public class Condition<T extends Comparable<T>> implements NestCondition {

  public AbstractQueryBuilder toQueryBuilder() {
    //TODO 策略模式进行重构
    if ("term".equalsIgnoreCase(op)) {
      return new TermQueryBuilder(this.getFieldName(), values.get(0));
    } else if ("range".equalsIgnoreCase(op)) {
      return new RangeQueryBuilder(this.getFieldName()).gte(values.get(0)).lte(values.get(1));
    } else if ("match".equals(op)) {
      return new MatchQueryBuilder(this.getFieldName(), values.get(0));
    }
    return null;
  }

  private String op;

  private String fieldName;

  private String type;

  private List<String> values;

  @Override
  @Transient
  public boolean isComplex() {
    return false;
  }


}
