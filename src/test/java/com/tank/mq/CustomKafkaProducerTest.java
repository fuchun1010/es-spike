package com.tank.mq;

import com.tank.dto.PersonDto;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomKafkaProducerTest {

  @Test
  public void testSendMessage() {
    PersonDto personDto = new PersonDto();
    personDto.setCardId("s0001").setGender(1).setJob("driver");
    val writeRecord = this.producer.sendMessage(personDto);
    Assert.assertTrue(writeRecord.writeSuccessfully());
  }

  @Before
  public void initCustomerKafkaProducer() {
    this.producer = new CustomKafkaProducer();
  }

  private CustomKafkaProducer producer;

}