package com.tank.dto;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import io.vavr.collection.Stream;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

/**
 * @author tank198435163.com
 */
public class OrderDtoTest {

  @Test
  public void convertToJavaBytes() {
    try (val out = new ByteArrayOutputStream();
         val objectOutputStream = new ObjectOutputStream(out)) {
      objectOutputStream.writeObject(this.orderDto);
      byte[] bytes = out.toByteArray();
      System.out.println(bytes.length);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void convertToProtoStuffBytes() {
    val schema = RuntimeSchema.getSchema(OrderDto.class);
    val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    byte[] bytes = ProtobufIOUtil.toByteArray(this.orderDto, schema, buffer);
    System.out.println(bytes.length);
  }

  @Test
  public void accumulateItemPrice() {
    val result = Stream.ofAll(this.orderDto.getItems())
            .map(OrderDto.Item::calculateItemPayment)
            .reduce(BigDecimal::add);
    System.out.println(result.doubleValue());
  }

  @Before
  public void init() {
    this.orderDto = new OrderDto();
    this.orderDto.setCategory("offline");
    this.orderDto.setOrderNo("s0001");
    this.orderDto.setStore("f0001");
    val item1 = new OrderDto.Item().setCode("f0001").setDesc("苹果").setUnitPrice("15").setWeight("12.5");
    val item2 = new OrderDto.Item().setCode("f0002").setDesc("樱桃").setUnitPrice("19").setWeight("18.5");
    val item3 = new OrderDto.Item().setCode("f0003").setDesc("车厘子").setUnitPrice("66").setWeight("13.2");
    Stream.of(item1, item2, item3).forEach(this.orderDto::addItem);
    val transFee = new OrderDto.OtherFee().setCategory(1).setPrice("15");
    val boxFee = new OrderDto.OtherFee().setCategory(2).setPrice("22");
    Stream.of(transFee, boxFee).forEach(this.orderDto::addOtherFee);
  }


  private OrderDto orderDto;
}