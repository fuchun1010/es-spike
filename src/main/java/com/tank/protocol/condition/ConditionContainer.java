package com.tank.protocol.condition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import com.tank.protocol.deserialization.ConditionContainerDeserializer;
import lombok.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.beans.Transient;
import java.util.List;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
@JsonDeserialize(using = ConditionContainerDeserializer.class)
public class ConditionContainer implements NestCondition {

  @Override
  @Transient
  public boolean isComplex() {
    return true;
  }

  public BoolQueryBuilder parse() {
    return this.parse(this);
  }

  @Transient
  public void addCondition(@NonNull final NestCondition condition) {
    this.conditions.add(condition);
  }

  @SneakyThrows
  private BoolQueryBuilder parse(@NonNull final ConditionContainer conditionContainer) {
    val logical = conditionContainer.getLogical();
    for (NestCondition condition : conditionContainer.conditions) {
      if (!condition.isComplex()) {
        Condition simple = ((Condition) condition);
        val termBuilder = simple.toQueryBuilder();
        val isOk = "must".equalsIgnoreCase(logical) ? this.next.must().add(termBuilder) : this.next.should().add(termBuilder);
        System.out.println("dd = ");
      } else {
        val newBuilder = QueryBuilders.boolQuery();
        this.next = "must".equalsIgnoreCase(logical) ? this.next.must(newBuilder) : this.next.should(newBuilder);
        this.next = newBuilder;
        ConditionContainer container = (ConditionContainer) condition;
        this.parse(container);
        this.next = this.boolBuilder;
      }
    }
    return this.boolBuilder;
  }


  private String logical = "must";

  private List<NestCondition> conditions = Lists.newArrayList();

  private BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

  private BoolQueryBuilder next = boolBuilder;

}
