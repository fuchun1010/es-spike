package com.tank.service.comparator;

import com.tank.protocol.condition.Condition;
import com.tank.service.ConditionBuilder;
import com.tank.service.CustomerBuilder;
import lombok.NonNull;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author tank198435163.com
 */
@Component("customerTermBuilder")
public class CustomerTermBuilder implements ConditionBuilder<String, TermQueryBuilder> {

  @Override
  public Optional<AbstractQueryBuilder<TermQueryBuilder>> toBuilder(@NonNull final Condition<String> condition) {

    return this.customerTermBuilder.toBuilder(condition,
            c -> this.supportOperation().equalsIgnoreCase(c),
            (fieldName, type, values) -> Optional.of(new TermQueryBuilder(fieldName, values.get(0)))
    );
  }

  @Override
  public String supportOperation() {
    return "term";
  }

  @Autowired
  private CustomerBuilder<String, TermQueryBuilder> customerTermBuilder;
}
