package com.tank.group;

import com.tank.util.ExecutablePrivateUtil;
import com.tank.util.StringWrapper;
import lombok.NonNull;
import lombok.val;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ExecutablePrivateUtilTest {

  @ParameterizedTest
  @ValueSource(strings = {"hello"})
  public void subStringNormallyTest(String str) {
    val executablePrivateUtil = new ExecutablePrivateUtil();
    val tryResult = executablePrivateUtil.<StringWrapper, String>invokeWithResult(StringWrapper.class, "subString", str, 1);
    val len = tryResult.map(String::length).getOrElse(0);
    Assert.assertTrue(len > 0);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ok,jack"})
  public void subStringWithoutParameterTest(@NonNull final String str) {
    Assert.assertEquals("ok,jack", str);
    val executablePrivateUtil = new ExecutablePrivateUtil();
    val tryResult = executablePrivateUtil.<StringWrapper, String>invokeWithResult(StringWrapper.class, "subString");
    val result = tryResult.map(String::length).getOrElse(-1);
    Assert.assertEquals((long) result, -1L);
  }


}
