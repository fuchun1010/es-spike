package com.tank.protocol.order;

import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class OrderReqTest {

  @Before
  public void init() {
    this.orderReq = new OrderReq();
  }

  @Test
  public void testToOrder() {
    OrderReq.PayLoadsBean ip = new OrderReq.PayLoadsBean();
    ip.setField("ip");
    ip.getValues().add("127.0.0.1");

    OrderReq.PayLoadsBean device = new OrderReq.PayLoadsBean();
    device.setField("deviceNo".toLowerCase());
    device.getValues().add("iphone6s");

    this.orderReq.getPayLoads().add(ip);
    this.orderReq.getPayLoads().add(device);

    try {
      val result = this.orderReq.toOrder();
      Assert.assertEquals(result.getIp(), "127.0.0.1");
      Assert.assertEquals(result.getDeviceNo(), "iphone6s");
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private OrderReq orderReq;
}