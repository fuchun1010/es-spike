package com.tank.util;

import io.vavr.control.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BinarySearchTest {

  @Test
  public void searchBy1() {
    Option<Integer> rsOpt = this.binarySearch.searchBy(arr, 1);
    Assert.assertFalse(rsOpt.isEmpty());
  }

  @Test
  public void searchBy2() {
    Option<Integer> rsOpt = this.binarySearch.searchBy(arr, 22);
    Assert.assertTrue(rsOpt.isEmpty());
  }

  @Test
  public void searchBy3() {
    Option<Integer> rsOpt = this.binarySearch.searchBy(arr, 21);
    Assert.assertFalse(rsOpt.isEmpty());
  }

  @Test
  public void searchBy4() {
    Option<Integer> rsOpt = this.binarySearch.searchBy(arr, 4);
    Assert.assertFalse(rsOpt.isEmpty());
  }

  @Before
  public void init() {
    this.binarySearch = new BinarySearch();
  }

  private BinarySearch binarySearch;

  private final Integer[] arr = {1, 2, 3, 4, 11, 21};
}