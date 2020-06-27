package com.tank.group;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.vavr.collection.Stream;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static java.lang.String.format;

public class GroupTest {

  @Test
  public void splitCharArrayTest() {
    String[] strArray = {"A1", "B1", "A2", "C2", "B3", "C1", "B2"};

    Arrays.sort(strArray, Comparator.comparing(s -> String.valueOf(s.charAt(0))));

    val result = Maps.<String, Set<String>>newHashMap();

    for (val str : strArray) {
      val firstCharacter = str.charAt(0);
      val values = result.computeIfAbsent(String.valueOf(firstCharacter), k -> Sets.newHashSet());
      values.add(str);
    }

    for (Map.Entry<String, Set<String>> entry : result.entrySet()) {
      val key = entry.getKey();
      val joiner = new StringJoiner(",");
      for (val element : entry.getValue()) {
        joiner.add(element);
      }
      System.out.println(format("%s -> (%s)", key, joiner.toString()));
    }

  }


  @Test
  public void reduceLeftTest() {
    Integer[] arr = {1, 2, 3, 4, 5};
    val result = Stream.of(arr).reduceLeft(Integer::sum);
    Assert.assertEquals(15, (int) result);
  }

}
