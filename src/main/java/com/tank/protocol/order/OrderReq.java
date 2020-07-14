package com.tank.protocol.order;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
public class OrderReq {

  @Getter
  @Setter
  public static class PayLoadsBean {

    public boolean isEmpty() {
      return Objects.isNull(field) || values.size() == 0 || field.trim().length() == 0;
    }

    private String field;
    private List<String> values = Lists.newArrayList();
  }

  public boolean isEmpty() {
    return this.payLoads.size() == 0;
  }

  private List<PayLoadsBean> payLoads = Lists.newArrayList();
}
