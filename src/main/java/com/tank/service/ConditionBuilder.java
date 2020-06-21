package com.tank.service;

import com.tank.protocol.condition.Condition;
import lombok.NonNull;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author tank198435163.com
 */
@Service
public interface ConditionBuilder<T extends Comparable<T>, QB extends AbstractQueryBuilder<QB>> {

  /**
   * 将条件转成elasticsearch condition
   *
   * @param condition
   * @return
   */
  Optional<AbstractQueryBuilder<QB>> toBuilder(@NonNull final Condition<T> condition);

  /**
   * 当前支持的操作
   *
   * @return
   */
  String supportOperation();

}
