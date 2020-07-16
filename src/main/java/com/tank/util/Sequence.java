package com.tank.util;

import com.google.common.collect.Queues;
import lombok.NonNull;
import lombok.val;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * @author tank198435163.com
 */
public class Sequence<T> {

  public void addItem(@NonNull final Supplier<T> supplier) {
    this.addItem(supplier.get());
  }

  public void addItem(@NonNull final T data) {
    this.reentrantLock.lock();
    try {
      val isFull = this.queue.size() == 200;
      if (isFull) {
        System.out.println(format("%s block", Thread.currentThread().getName()));
        this.consumeCondition.signalAll();
        this.produceCondition.await();
      }
      System.out.println(String.format("%s produce:%s", Thread.currentThread().getName(), data));
      this.queue.add(data);
      this.consumeCondition.signalAll();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      reentrantLock.unlock();
    }
  }

  public Optional<T> fetch() {
    this.reentrantLock.lock();
    try {
      val isEmpty = this.queue.isEmpty();
      if (isEmpty) {
        System.out.println(format("%s block", Thread.currentThread().getName()));
        this.produceCondition.signalAll();
        this.consumeCondition.await();
      }
      return Optional.ofNullable(this.queue.poll());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.reentrantLock.unlock();
    }

    return Optional.empty();
  }

  private final ReentrantLock reentrantLock = new ReentrantLock(true);

  private final Condition produceCondition = reentrantLock.newCondition();

  private final Condition consumeCondition = reentrantLock.newCondition();

  private final Queue<T> queue = Queues.newArrayDeque();

}
