package com.tank.service.comparator;

import com.tank.protocol.condition.Condition;
import com.tank.service.ConditionBuilder;
import com.tank.service.CustomerBuilder;
import lombok.NonNull;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author tank198435163.com
 */
@Component("customerMatchBuilder")
public class CustomerMatchBuilder implements ConditionBuilder<String, MatchQueryBuilder> {

  @Override
  public Optional<AbstractQueryBuilder<MatchQueryBuilder>> toBuilder(@NonNull Condition<String> condition) {
    return this.matchCustomerBuilder.toBuilder(condition,
            op -> this.supportOperation().equals(op),
            (fieldName, type, values) -> Optional.of(new MatchQueryBuilder(fieldName, values.get(0))));
  }

  @Override
  public String supportOperation() {
    return "match";
  }

  @Autowired
  private CustomerBuilder<String, MatchQueryBuilder> matchCustomerBuilder;
}
