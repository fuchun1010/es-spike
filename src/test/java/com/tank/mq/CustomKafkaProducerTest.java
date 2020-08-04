package com.tank.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tank.dto.OrderDto;
import com.tank.dto.PersonDto;
import com.tank.mq.guess.MyProducer;
import com.tank.mq.guess.ProtoStuffSerial;
import io.vavr.collection.Stream;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomKafkaProducerTest {

  @Test
  public void testSendMessage() {
    PersonDto personDto = new PersonDto();
    personDto.setCardId("s0009").setGender(1).setJob("driver9");
    val writeRecord = this.producer.sendMessage(personDto);
    Assert.assertTrue(writeRecord.writeSuccessfully());
  }

  @Test
  public void testSendOrderMessageWithJson() {
    val orderDto = defaultOrderDto();
    val orderProducer = new MyProducer<String, String>();
    try {
      val result = orderProducer.defaultProducer().<OrderDto>sendMessage("order", orderDto, this.jsonMapper::writeValueAsString);
      System.out.println(result.toString());
      Assert.assertTrue(result.writeSuccessfully());
    } finally {
      orderProducer.close();
    }

  }

  @Test
  public void testSendOrderWithBytes() {
    val orderDto = this.defaultOrderDto();
    val orderProducer = new MyProducer<String, OrderDto>();
    try {
      val result = orderProducer.customProducer(ProtoStuffSerial.class).sendMessage("order", orderDto, dto -> dto);
      System.out.println("offset is:====>" + result.getOffset());
      System.out.println(result.toString());
      Assert.assertTrue(result.writeSuccessfully());
    } finally {
      orderProducer.close();
    }
  }

  private OrderDto defaultOrderDto() {
    val orderDto = new OrderDto().setCategory("offline").setStore("f0001").setOrderNo("s0003");
    val item1 = new OrderDto.Item().setCode("f0001").setDesc("苹果").setUnitPrice("15").setWeight("12.5");
    val item2 = new OrderDto.Item().setCode("f0002").setDesc("樱桃").setUnitPrice("19").setWeight("18.5");
    val item3 = new OrderDto.Item().setCode("f0003").setDesc("车厘子").setUnitPrice("66").setWeight("13.2");
    Stream.of(item1, item2, item3).forEach(orderDto::addItem);
    val transFee = new OrderDto.OtherFee().setCategory(1).setPrice("15");
    val boxFee = new OrderDto.OtherFee().setCategory(2).setPrice("22");
    Stream.of(transFee, boxFee).forEach(orderDto::addOtherFee);
    return orderDto;
  }

  @Before
  public void initCustomerKafkaProducer() {
    this.producer = new CustomKafkaProducer();
    this.jsonMapper = new ObjectMapper();
  }

  private CustomKafkaProducer producer;
  private ObjectMapper jsonMapper;

}