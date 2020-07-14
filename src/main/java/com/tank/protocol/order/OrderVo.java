package com.tank.protocol.order;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
public class OrderVo {

  private String indexer;

  private String orderNo;

  private String ip;

  private String deviceNo;

  private String unionId;

  private String customerId;

  private String createDateTime;
}
