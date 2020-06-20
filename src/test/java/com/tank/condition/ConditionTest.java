package com.tank.condition;

import com.tank.protocol.condition.Condition;
import com.tank.protocol.condition.ConditionTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author tank198435163.com
 */
public class ConditionTest {

  @Test
  public void defaultSomeCondition() {
    Condition<String> simple = new Condition<>();
    simple.setOp("range");
    simple.setType("text");
    simple.setFieldName("commentDateTime");
    simple.setValues(Arrays.asList("2019-12-11 13:12:11", "2019-12-12 13:12:11"));

    ConditionTree node = new ConditionTree();
    Condition<String> c1 = new Condition<>();
    c1.setOp("match");
    c1.setFieldName("storeCode");
    c1.setType("text");
    c1.setValues(Arrays.asList("0087", "0086"));

    node.setLogical("must");
    node.addCondition(c1);

    this.conditionTree.addCondition(simple);
    this.conditionTree.addCondition(node);

    Assert.assertEquals(this.conditionTree.getConditions().size(), 2);
  }


  @Before
  public void init() {
    this.conditionTree = new ConditionTree();
  }


  private ConditionTree conditionTree;

}
