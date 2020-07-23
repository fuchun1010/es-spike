package com.tank.util;

import lombok.val;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeDurationTest {

  @Test
  public void testRandomSeconds() {
    val random = ThreadLocalRandom.current();
    IntStream.range(1, 10).boxed().map(i -> random.nextInt(100)).forEach(System.out::println);
  }

  @Test
  public void processBy() throws InterruptedException {
    val timeDuration = new TimeDuration<Integer>();
    val latch = new CountDownLatch(1);
    val threadPool = Executors.newFixedThreadPool(1);
    ThreadLocalRandom random = ThreadLocalRandom.current();
    threadPool.submit(() -> {
      IntStream.rangeClosed(1, 10).forEach(i -> {
        try {
          Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        timeDuration.getRecords().add(i);
      });
    });

    timeDuration.processBy(timeDuration.getRecords(), records -> {
      val list = records.stream().map(String::valueOf).collect(Collectors.toList());
      val result = String.join(",", list);
      System.out.println(result);
      latch.countDown();
    });

    latch.await();
    System.out.println("over");
  }
}