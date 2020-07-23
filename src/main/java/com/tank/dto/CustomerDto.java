package com.tank.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author tank198435163.com
 */

public class CustomerDto implements Serializable {

  private static final long serialVersionUID = 4096075502016983671L;

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private int gender;

}
