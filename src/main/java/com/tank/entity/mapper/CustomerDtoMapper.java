package com.tank.entity.mapper;

import com.tank.dto.CustomerDto;
import com.tank.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.validation.constraints.NotNull;

/**
 * @author tank198435163.com
 */
@Mapper
public interface CustomerDtoMapper {

  /**
   * convert dto to vo
   *
   * @param customerDto
   * @return
   */
  @Mapping(source = "username", target = "name")
  @Mapping(source = "gender", target = "gender")
  Customer toCustomer(@NotNull final CustomerDto customerDto);


  /**
   * convert vo to dto
   *
   * @param customer
   * @return
   */
  @Mapping(source = "name", target = "username")
  @Mapping(source = "gender", target = "gender")
  CustomerDto toCustomerDTO(@NotNull final Customer customer);


}
