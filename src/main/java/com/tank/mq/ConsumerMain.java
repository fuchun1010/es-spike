package com.tank.mq;

import lombok.val;

/**
 * @author tank198435163.com
 */
public class ConsumerMain {
  public static void main(final String[] args) {
    val customKafkaConsumer = new CustomKafkaConsumer();
    customKafkaConsumer.acceptMessage();
  }
}
