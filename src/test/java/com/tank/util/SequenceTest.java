package com.tank.util;

import lombok.val;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class SequenceTest {

  
  @Test
  public void testConsume() throws InterruptedException {
    val latch = new CountDownLatch(1);
    val sequence = new Sequence<Integer>();

    val producer = new Thread(() -> {
      IntStream.range(0, 200).boxed().forEach(sequence::addItem);
    }, "producer");

    val consumer = new Thread(() -> {
      for (; ; ) {
        sequence.fetch().ifPresent(data -> System.out.println(String.format("%s consume:%d", Thread.currentThread().getName(), data)));
      }
    }, "consumer");

    producer.start();
    consumer.start();
    latch.await();
  }


}