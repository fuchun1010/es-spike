package com.tank.dto;

import com.google.common.collect.Lists;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrderDto implements Serializable {

  @Transient
  public void addItem(final @NonNull Item item) {
    this.items.add(item);
  }

  @Transient
  public void addOtherFee(final @NonNull OtherFee otherFee) {
    this.others.add(otherFee);
  }


  /**
   * orderNo : 1000
   * store : s100
   * category : offline
   * itmes : [{"code":"f0001","weight":"15.5","desc":"苹果","unitPrice":"12"},{"code":"f0002","weight":"12","desc":"桃子","unitPrice":"15"},{"code":"f0003","weight":"18.5","desc":"樱桃","unitPrice":"12"}]
   * others : [{"category":1,"price":16},{"category":2,"price":13}]
   */

  private String orderNo;
  private String store;
  private String category;
  private List<Item> items = Lists.newArrayList();
  private List<OtherFee> others = Lists.newArrayList();


  @Getter
  @Setter
  @Accessors(chain = true)
  public static class Item implements Serializable {

    @Transient
    public BigDecimal calculateItemPayment() {
      val tmpWeight = Try.of(() -> Double.parseDouble(getWeight()))
              .map(BigDecimal::valueOf)
              .getOrElse(BigDecimal.ZERO);

      val unitPrice = Try.of(() -> Double.parseDouble(getUnitPrice())).map(BigDecimal::valueOf)
              .getOrElse(BigDecimal.ZERO);

      return tmpWeight.multiply(unitPrice);
    }

    /**
     * code : f0001
     * weight : 15.5
     * desc : 苹果
     * unitPrice : 12
     */

    private String code;
    private String weight;
    private String desc;
    private String unitPrice;

  }

  @Getter
  @Setter
  @Accessors(chain = true)
  public static class OtherFee implements Serializable {
    /**
     * category : 1
     * price : 16
     */

    private int category;
    private String price;

  }
}
