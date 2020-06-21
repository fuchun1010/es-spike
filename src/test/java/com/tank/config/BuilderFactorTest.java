package com.tank.config;

import com.tank.protocol.condition.Condition;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @author tank198435163.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BuilderFactorTest {

  @Test
  public void testMatchCondition() {
    val condition = new Condition<String>();
    condition.setOp("match");
    condition.setType("text");
    condition.setFieldName("username");
    condition.setValues(Collections.singletonList("jack"));
    val matchBuilderOpt = this.builderFactor.fetchBuilder(condition);
    Assert.assertTrue(matchBuilderOpt.isPresent());
  }

  @Autowired
  private BuilderFactor builderFactor;
}