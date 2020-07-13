package com.tank.dto;

import com.tank.entity.Customer;
import com.tank.entity.mapper.CustomerDtoMapper;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

/**
 * @author tank198435163.com
 */
public class CustomerDtoTest {

  @Test
  public void testDtoConvertToVo() {
    val customerDto = new CustomerDto();
    customerDto.setGender(1);
    customerDto.setUsername("tank");
    val dtoMapper = Mappers.getMapper(CustomerDtoMapper.class);
    Assert.assertNotNull(dtoMapper);
    val customer = dtoMapper.toCustomer(customerDto);
    Assert.assertEquals(customerDto.getUsername(), customer.getName());
  }

  @Test
  public void testVoConvertToDTO() {
    val voMapper = Mappers.getMapper(CustomerDtoMapper.class);
    Assert.assertNotNull(voMapper);
    val customer = new Customer();
    customer.setGender("1");
    customer.setName("jack");

    val dto = voMapper.toCustomerDTO(customer);
    Assert.assertNotNull(dto);
    Assert.assertEquals(dto.getUsername(), customer.getName());
    Assert.assertEquals(String.valueOf(dto.getGender()), customer.getGender());
  }

}