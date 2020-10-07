package com.tank.guess;

import io.vavr.CheckedConsumer;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.val;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;

public class MyObserverTest {


  @Test
  public void testDispatchMsg() {
    final Observer jack = (observable, message) -> System.out.println("jack receive message:" + message.toString());
    final Observer john = (observable, message) -> System.out.println("john receive message:" + message.toString());
    val observerContainer = new ObserverContainer();
    observerContainer.addObserver(jack, john);
    observerContainer.notifyAllObservers("this is received message");
  }

  @Test
  public void testNull() {
    val result = Option.of(null).getOrElse("-");
    System.out.println(result);
  }

  @Test
  public void testGroup() {
    Tuple2<String, String> v1 = new Tuple2<String, String>("1", "hello");
    Tuple2<String, String> v2 = new Tuple2<String, String>("2", "hello2");
    Tuple2<String, String> v3 = new Tuple2<String, String>("1", "hello3");

    Stream.of(v1, v2, v3).groupBy(Tuple2::_1)
            .get("1")
            .map(Stream::toList)
            .forEach(d -> this.print(d, element -> {
              val tips = String.format("key is:[%s], value is:[%s]", element._1(), element._2());
              System.out.println(tips);
            }));

  }

  @Test
  public void testMillions() {
    val millions = System.currentTimeMillis();
    val instant = Instant.ofEpochMilli(millions);
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    val result = dateTimeFormatter.format(dateTime);
    System.out.println(result);
  }

  private <T> void print(@NonNull final List<T> list,
                         @NonNull final CheckedConsumer<T> consumer) {
    T data = list.head();
    if (data != null) {
      try {
        consumer.accept(data);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
    val isEmpty = list.tail().isEmpty();
    if (!isEmpty) {
      this.print(list.tail(), consumer);
    }
  }


  private static class ObserverContainer extends Observable {

    /**
     * @param observers
     * @param <T>
     */
    @SafeVarargs
    public final <T extends Observer> void addObserver(@NonNull final T... observers) {
      Stream.of(observers).forEach(this::addObserver);
    }

    public <T> void notifyAllObservers(@NonNull final T message) {
      synchronized (this) {
        super.setChanged();
        super.notifyObservers(message);
      }
    }

  }

}
