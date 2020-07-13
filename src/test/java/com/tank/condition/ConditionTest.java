package com.tank.condition;

import com.tank.protocol.condition.Condition;
import com.tank.protocol.condition.ConditionContainer;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @author tank198435163.com
 */
public class ConditionTest {

  private ConditionContainer defaultContainer() {
    Condition<String> simple = new Condition<>();
    simple.setOp("range");
    simple.setType("text");
    simple.setFieldName("commentDateTime");
    simple.setValues(Arrays.asList("2019-12-11 13:12:11", "2019-12-12 13:12:11"));

    ConditionContainer node = new ConditionContainer();
    Condition<String> c1 = new Condition<>();
    c1.setOp("match");
    c1.setFieldName("storeCode");
    c1.setType("text");
    c1.setValues(Arrays.asList("0087", "0086"));

    node.setLogical("should");
    node.addCondition(c1);

    this.conditionTree.addCondition(simple);
    this.conditionTree.addCondition(node);

    return this.conditionTree;
  }

  @Test
  public void parseCondition() {
    val result = this.defaultContainer().parse();
    System.out.println("xx");
  }

  @Before
  public void init() {
    this.conditionTree = new ConditionContainer();
  }

  private ConditionContainer conditionTree;

}
