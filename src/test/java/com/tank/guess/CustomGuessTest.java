package com.tank.guess;

import com.tank.dto.PersonDto;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

public class CustomGuessTest {

  @Test
  public void testObject2ByteArray() throws IOException {

    try (val bytesStream = new ByteArrayOutputStream(); val out = new ObjectOutputStream(bytesStream);) {
      out.writeObject(this.personDto);
      out.flush();
      byte[] objectBytes = bytesStream.toByteArray();
      Assert.assertTrue(objectBytes.length > 0);
      ByteBuffer buffer = ByteBuffer.wrap(objectBytes);
      byte[] target = new byte[objectBytes.length];
      buffer.get(target);
      val in = new ByteArrayInputStream(target);
      val objIn = new ObjectInputStream(in);
      val obj = objIn.readObject();
      Assert.assertTrue(obj instanceof PersonDto);
      val person = ((PersonDto) obj);
      Assert.assertTrue("driver".equalsIgnoreCase(person.getJob()));
    } catch (final Exception exp) {
      exp.printStackTrace();
    }

  }


  @Before
  public void init() {
    this.personDto = new PersonDto();
    this.personDto.setJob("driver").setGender(1).setCardId("500105198403050611");
  }

  private PersonDto personDto;

}
