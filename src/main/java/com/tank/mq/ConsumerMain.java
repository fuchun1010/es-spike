package com.tank.mq;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tank.mq.guess.assigin.SimpleConsumer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * @author tank198435163.com
 */
@Slf4j
public class ConsumerMain {
  public static void main(final String[] args) {
//    val customKafkaConsumer = new CustomKafkaConsumer();
//    customKafkaConsumer.fetchMessageFrom("demo", 0, 0L);

//    val consumer = new MyConsumer<String, String>();
    val mapper = new JsonMapper();
//    consumer.defaultJsonDeSerial().<OrderDto>handleReceivedMessage("order", json -> {
//      val orderDto = Try.of(() -> mapper.readValue(json.getBytes(), OrderDto.class)).get();
//      System.out.println("order no ==========> " + orderDto.getOrderNo());
//    });
//    val customKafkaConsumer = new MyConsumer<String, OrderDto>();
//    customKafkaConsumer.customDeSerial().handleReceivedMessage("order", orderDto -> {
//      val json = mapper.writeValueAsString(orderDto);
//      log.info("order dto json:[{}]", json);
//    });
    val simpleConsumer = new SimpleConsumer();
    // simpleConsumer.consume();
    val result = simpleConsumer.readAssignOffsetValue("demo", 0, 1L).getOrElse(() -> "-");
    System.out.println("result = " + result);
  }
}
