package com.tank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author tank198435163.com
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class WriteRecord {

  public boolean writeSuccessfully() {
    return offset != -1;
  }

  private String topicName;

  private int partitionId;

  private long offset = -1L;
}
