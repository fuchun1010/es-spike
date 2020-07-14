package com.tank.protocol.order;

import com.google.common.collect.Lists;
import io.vavr.collection.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
public class OrderReq {

  public OrderVo toOrder() throws InvocationTargetException, IllegalAccessException {
    val orderVo = new OrderVo();
    if (this.isEmpty()) {
      throw new IllegalArgumentException("payLoads 不允许是空值");
    }
    val methods = OrderVo.class.getDeclaredMethods();
    val methodMapping = Stream.of(methods).filter(method -> !method.getName().startsWith("get")).collect(Collectors.groupingBy(m -> m.getName().toLowerCase()));

    for (PayLoadsBean payLoad : this.payLoads) {
      val methodName = format("set%s", payLoad.getField());
      val targets = methodMapping.get(methodName);
      if (Objects.isNull(targets) || targets.isEmpty()) {
        continue;
      }

      val method = targets.get(0);
      method.invoke(orderVo, payLoad.getValues().size() == 1 ? payLoad.getValues().get(0) : payLoad.getValues());
    }

    return orderVo;
  }

  @Getter
  @Setter
  public static class PayLoadsBean {

    public boolean isEmpty() {
      return Objects.isNull(field) || values.size() == 0 || field.trim().length() == 0;
    }

    private String field;
    private List<String> values = Lists.newArrayList();
  }

  private boolean isEmpty() {
    return this.payLoads.size() == 0;
  }

  private List<PayLoadsBean> payLoads = Lists.newArrayList();
}
