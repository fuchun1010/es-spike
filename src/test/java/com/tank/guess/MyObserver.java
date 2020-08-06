package com.tank.guess;

import io.vavr.collection.Stream;
import lombok.NonNull;
import lombok.val;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

public class MyObserver {


  @Test
  public void testDispatchMsg() {
    final Observer jack = (observable, message) -> System.out.println("jack receive message:" + message.toString());
    final Observer john = (observable, message) -> System.out.println("john receive message:" + message.toString());
    val observerContainer = new ObserverContainer();
    observerContainer.addObserver(jack, john);
    observerContainer.notifyAllObservers("this is received message");
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
