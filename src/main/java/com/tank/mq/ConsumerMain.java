package com.tank.mq;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tank.dto.OrderDto;
import com.tank.mq.guess.MyConsumer;
import io.vavr.control.Try;
import lombok.val;

/**
 * @author tank198435163.com
 */
public class ConsumerMain {
  public static void main(final String[] args) {
//    val customKafkaConsumer = new CustomKafkaConsumer();
//    customKafkaConsumer.fetchMessageFrom("demo", 0, 0L);

    val consumer = new MyConsumer<String, String>();
    val mapper = new JsonMapper();
    consumer.defaultJsonDeSerial().<OrderDto>handleReceivedMessage("order", json -> {
      val orderDto = Try.of(() -> mapper.readValue(json.getBytes(), OrderDto.class)).get();
      System.out.println("order no ==========> " + orderDto.getOrderNo());
    });
  }
}
