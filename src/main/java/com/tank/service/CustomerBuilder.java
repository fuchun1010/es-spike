package com.tank.service;

import com.tank.protocol.condition.Condition;
import io.vavr.Function3;
import lombok.NonNull;
import lombok.val;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author tank198435163.com
 */
@Component
public class CustomerBuilder<T extends Comparable<T>, QB extends AbstractQueryBuilder<QB>> {

  public Optional<AbstractQueryBuilder<QB>> toBuilder(@NonNull final Condition<T> condition,
                                                      @NonNull final Predicate<String> predicate,
                                                      @NonNull final Function3<String, String, List<String>, Optional<AbstractQueryBuilder<QB>>> fun) {

    val op = condition.getOp();
    val fieldName = condition.getFieldName();
    val type = condition.getType();
    val values = condition.getValues();
    return predicate.test(op) ? fun.apply(fieldName, type, values) : Optional.empty();
  }

}
