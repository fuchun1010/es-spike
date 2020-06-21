package com.tank.service.comparator;

import com.tank.protocol.condition.Condition;
import com.tank.service.ConditionBuilder;
import com.tank.service.CustomerBuilder;
import lombok.NonNull;
import lombok.val;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author tank198435163.com
 */
@Component("customerRangeBuilder")
public class CustomerRangeBuilder implements ConditionBuilder<String, RangeQueryBuilder> {

  @Override
  public Optional<AbstractQueryBuilder<RangeQueryBuilder>> toBuilder(@NonNull final Condition<String> condition) {
    return this.customerRangeBuilder.toBuilder(condition,
            op -> this.supportOperation().equalsIgnoreCase(op),
            (fieldName, type, values) -> {
              val rangeQueryBuilder = new RangeQueryBuilder(condition.getFieldName()).gte(values.get(0)).lte(values.get(1));
              return Optional.ofNullable(rangeQueryBuilder);
            });
  }

  @Override
  public String supportOperation() {
    return "range";
  }

  @Autowired
  private CustomerBuilder<String, RangeQueryBuilder> customerRangeBuilder;

}
