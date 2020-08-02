package com.tank.dto;

import com.google.common.base.MoreObjects;
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("topicName", this.topicName)
            .add("partitionId", this.partitionId).add("offset", this.offset).toString();
  }
}
