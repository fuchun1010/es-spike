package com.tank.protocol;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = {"orderNo"})
public class Order implements Serializable {

  /**
   * orderNo : 1
   * orgCode : 0087
   * items : [{"snapshotId":"s0001","weight":20}]
   * score : 5
   * comment : 这个还可以
   */

  private String orderNo;
  private String orgCode;
  private int score;
  private String comment;
  private List<ItemsBean> items = Lists.newArrayList();

  @Setter
  @Getter
  @Accessors(chain = true)
  public static class ItemsBean {
    /**
     * snapshotId : s0001
     * weight : 20
     */
    private String snapshotId;
    private int weight;
  }
}
