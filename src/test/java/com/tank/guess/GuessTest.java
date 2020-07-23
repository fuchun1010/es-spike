package com.tank.guess;

import com.google.common.collect.Lists;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author tank198435163.com
 */
public class GuessTest {

  @Test
  public void testSystemDir() {
    val dirPath = System.getProperty("user.dir");
    Assert.assertNotNull(dirPath);
    Assert.assertTrue(dirPath.trim().length() > 0);
    System.out.println(dirPath);
  }

  @Test
  public void testStream() {
    val lists = Lists.newArrayList();
    lists.add("hello");
    lists.add("the");
    lists.add("world");
    try (val byteOut = new ByteArrayOutputStream(); val out = new ObjectOutputStream(byteOut)) {
      out.writeObject(lists);
      val result = byteOut.toByteArray();
      System.out.println(result.length);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
