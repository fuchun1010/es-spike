package com.tank.config;

import com.google.common.collect.Maps;
import com.tank.protocol.condition.Condition;
import com.tank.service.ConditionBuilder;
import com.tank.service.comparator.CustomerMatchBuilder;
import com.tank.service.comparator.CustomerRangeBuilder;
import com.tank.service.comparator.CustomerTermBuilder;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

/**
 * @author tank198435163.com
 */
@Configuration
public class BuilderFactor implements InitializingBean {

  @SuppressWarnings("unchecked")
  public Optional<AbstractQueryBuilder> fetchBuilder(Condition<String> condition) {
    return this.builderFactor.get(condition.getOp()).toBuilder(condition);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Map<String, ConditionBuilder> builderFactor = Maps.newHashMap();
    builderFactor.putIfAbsent(this.customerMatchBuilder.supportOperation(), this.customerMatchBuilder);
    builderFactor.putIfAbsent(this.customerRangeBuilder.supportOperation(), this.customerRangeBuilder);
    builderFactor.putIfAbsent(this.customerMatchBuilder.supportOperation(), this.customerTermBuilder);
    this.builderFactor = builderFactor;
  }

  @Autowired
  @Qualifier("customerMatchBuilder")
  private CustomerMatchBuilder customerMatchBuilder;

  @Autowired
  @Qualifier("customerRangeBuilder")
  private CustomerRangeBuilder customerRangeBuilder;

  @Autowired
  @Qualifier("customerTermBuilder")
  private CustomerTermBuilder customerTermBuilder;

  private Map<String, ConditionBuilder> builderFactor;

}
