package com.tank.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.vavr.CheckedConsumer;
import io.vavr.Tuple2;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tank198435163.com
 */
public class TimeDuration<T> {

  public void processBy(
          @NonNull final List<T> records,
          @NonNull final CheckedConsumer<List<T>> consumer) {

    lock.lock();
    try {
      this.condition = lock.newCondition();
      var startTime = System.currentTimeMillis();
      for (; ; ) {
        val endTime = System.currentTimeMillis();

        var diff = endTime - startTime;
        System.out.println("start = " + startTime + ", endTime = " + endTime + " , diff = " + diff);
        if (diff > 1000 || records.size() == 10) {
          startTime = System.currentTimeMillis();
          consumer.accept(records);

          records.clear();
        }
      }
    } catch (final Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      lock.unlock();
    }

  }

  private void print(@NonNull final Map<String, String> maps) {
    val stringBuilder = new StringBuilder();
    for (Map.Entry<String, String> entry : maps.entrySet()) {
      stringBuilder.append(entry.getKey());
      stringBuilder.append(":");
      stringBuilder.append(entry.getValue());
      stringBuilder.append(" ");
    }
    System.out.println(stringBuilder.toString());
  }


  @SafeVarargs
  private final void print(@NonNull final Tuple2<String, String>... tuple2s) {
    val maps = Maps.<String, String>newHashMap();
    for (Tuple2<String, String> tuple2 : tuple2s) {
      maps.putIfAbsent(tuple2._1(), tuple2._2());
    }
    this.print(maps);
  }

  @Getter
  @Setter
  private List<T> records = Lists.newArrayList();

  private ReentrantLock lock = new ReentrantLock(true);

  private Condition condition;
}
