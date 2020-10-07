package com.tank.algorithm;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//algorithm
public class AlgorithmTest {


  @Test
  public void testDuplicateIndex() {
    //1,1,1,3,3,5,5 => 1,3,5 => 1,2,4,6
    final Integer[] array = {1, 1, 1, 3, 3, 5, 5};
    val arrayWrapper = new ArrayWrapper<>(array);
    val indexes = arrayWrapper.returnDuplicationItemIndex();
    System.out.println(indexes);
  }

  @Test
  public void testDuplicateIndex2() {
    final Integer[] array = {1, 3, 3, 5};
    val arrayWrapper = new ArrayWrapper<>(array);
    val indexes = arrayWrapper.returnDuplicationItemIndex();
    System.out.println(indexes);
  }

  @Test
  public void testDuplicateIndex3() {
    final Integer[] array = {1, 3, 5, 5};
    val arrayWrapper = new ArrayWrapper<>(array);
    val indexes = arrayWrapper.returnDuplicationItemIndex();
    System.out.println(indexes);
  }

  @Test
  public void testDeleteDuplicationItem() {
    final Integer[] array = {1, 1, 1, 3, 3, 5, 5};
    val arrayWrapper = new ArrayWrapper<>(array);
    val results = arrayWrapper.deleteDuplicationItem(Integer.class);
    Assert.assertNotNull(results);
    System.out.println(Arrays.toString(results));
  }

  //1,2,3,4  //2,3
  private static class ArrayWrapper<T extends Comparable<T>> {

    public ArrayWrapper(@NonNull final T[] target) {
      super();
      this.target = target;
      if (this.target.length == 0) {
        throw new IllegalArgumentException("array must not empty");
      }
      Arrays.sort(this.target);
    }

    public T[] deleteDuplicationItem(@NonNull final Class<T> clazz) {
      List<Integer> targetIndex = this.uniqueIndex();
      return this.copyArray(targetIndex, clazz);
    }


    public List<Integer> returnDuplicationItemIndex() {
      val indexes = Lists.<Integer>newArrayList();
      int length = this.target.length;
      T start, tmp;
      int step = 1;
      for (int i = 0; i < length; i += step) {
        start = this.target[i];
        step = 1;
        for (int j = i + 1; j < length; j++) {
          tmp = this.target[j];
          if (tmp.compareTo(start) == 0) {
            indexes.add(j);
            step += 1;
          } else {
            break;
          }
        }
      }
      return indexes;
    }


    private List<Integer> uniqueIndex() {
      val duplicatedIndexes = this.returnDuplicationItemIndex();
      val uniqueIndexes = Lists.<Integer>newArrayList();
      for (int i = 0; i < this.target.length; i++) {
        if (!duplicatedIndexes.contains(i)) {
          uniqueIndexes.add(i);
        }
      }

      return uniqueIndexes;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private T[] copyArray(@NonNull final List<Integer> targetIndexed, @NonNull final Class<T> clazz) {
      T[] data = ((T[]) Array.newInstance(clazz, targetIndexed.size()));
      val results = targetIndexed.stream().map(index -> this.target[index])
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
      results.toArray(data);
      return data;

    }

    private T[] target;

  }


}
