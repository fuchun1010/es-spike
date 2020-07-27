package com.tank.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
@Accessors(chain = true)
public class PersonDto implements StandDto {

  @Override
  public String fetchId() {
    return this.getCardId();
  }

  private String cardId;

  private int gender;

  private String job;
}
