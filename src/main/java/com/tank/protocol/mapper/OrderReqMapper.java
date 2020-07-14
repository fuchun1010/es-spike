package com.tank.protocol.mapper;

import com.tank.protocol.order.OrderReq;
import com.tank.protocol.order.OrderVo;
import io.vavr.collection.Stream;
import lombok.NonNull;
import lombok.val;
import org.mapstruct.Mapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author tank198435163.com
 */
@Mapper
public interface OrderReqMapper {

  /**
   * mapping to Order Value object
   *
   * @param orderReq
   * @return
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  default OrderVo toOrderVo(@NonNull final OrderReq orderReq) throws InvocationTargetException, IllegalAccessException {
    val payLoads = orderReq.getPayLoads();
    val orderVo = new OrderVo();
    if (orderReq.isEmpty()) {
      throw new IllegalArgumentException("payLoads 不允许是空值");
    }
    val methods = OrderVo.class.getDeclaredMethods();
    val methodMapping = Stream.of(methods).filter(method -> !method.getName().startsWith("get")).collect(Collectors.groupingBy(m -> m.getName().toLowerCase()));

    for (OrderReq.PayLoadsBean payLoad : payLoads) {
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
}
