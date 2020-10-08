package com.tank.pc;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ProducerConsumerTest {

  @Test
  @SneakyThrows
  public void testDequeue() {
    val queue = new ArrayDeque<Integer>();
    queue.push(1);
    queue.push(2);
    while (!queue.isEmpty()) {
      System.out.println(queue.removeLast());
    }

  }

  @Test
  @SneakyThrows
  public void testSimpleProducerConsumer() {
    val simpleProducerConsumer = new SimpleProducerConsumer<Integer>();
    //val threadA = new Thread(() -> IntStream.rangeClosed(0, 99).boxed().forEach(simpleProducerConsumer::push), "ThreadA");
    IntStream.rangeClosed(0, 99).boxed().forEach(simpleProducerConsumer::push);
    val threadB = new Thread(simpleProducerConsumer::take, "ThreadB");
    //threadA.start();
    threadB.start();
    System.in.read();
    simpleProducerConsumer.stop();
  }


  private static class SimpleProducerConsumer<T> {

    public SimpleProducerConsumer(@NonNull final Integer capacity) {
      super();
      this.capacity = Objects.isNull(capacity) || capacity.compareTo(0) == 0 ? MAX_SIZE : capacity;
    }

    public SimpleProducerConsumer() {
      this.capacity = MAX_SIZE;
    }

    public void stop() {
      this.running.set(false);
    }


    @SneakyThrows
    public void push(@NonNull final T data) {
      this.reentrantLock.lock();
      try {
        val isFull = this.queue.size() == this.capacity;
        while (isFull) {
          System.out.println("queue full");
          this.consumerCondition.signalAll();
          this.producerCondition.await();
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " produce " + data);
        this.queue.add(data);
        this.consumerCondition.signalAll();
      } finally {
        this.reentrantLock.unlock();
      }
    }

    @SneakyThrows
    public T take() {
      this.reentrantLock.lock();
      try {
        while (running.get()) {
          while (this.queue.isEmpty()) {
            this.producerCondition.signalAll();
            this.consumerCondition.await();
          }
          final T result = this.queue.remove();
          this.producerCondition.signalAll();
          System.out.println("consumer thread name is :" + Thread.currentThread().getName() + " result = " + result);
        }
      } finally {
        this.reentrantLock.unlock();
      }
      return null;
    }


    private final ReentrantLock reentrantLock = new ReentrantLock(false);

    private final Condition producerCondition = reentrantLock.newCondition();
    private final Condition consumerCondition = reentrantLock.newCondition();

    private final Integer MAX_SIZE = 500;

    private final Integer capacity;

    private final ArrayDeque<T> queue = new ArrayDeque<>();

    private AtomicBoolean running = new AtomicBoolean(true);
  }

}
