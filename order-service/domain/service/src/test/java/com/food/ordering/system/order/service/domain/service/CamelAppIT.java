package com.food.ordering.system.order.service.domain.service;


import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommandDTO;
import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;

  @Test
  @Disabled
  @SneakyThrows
  public void checkCustomer() {

    var mock = this.context.getEndpoint("mock:result", MockEndpoint.class);
    //

    AdviceWith.adviceWith(this.context, "checkCustomerCMDH", r -> {
      //r.replaceFromWith("direct:orderCreateCommandHandler");
      r.weaveAddLast().to("mock:result");
      //  r.mockEndpoints("stream*");
    });
    //
    mock.expectedMessageCount(1);
    //mock.expectedBodiesReceived("Hello World");
    // UUID's based on DB
    var customerIdValid = UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9");
    //var customerIdInValid = UUID.fromString("55fcdc36-26a0-4752-b957-0057a1ae6929");
    //
    var body = CreateOrderCommandDTO.builder()
            .customerId(customerIdValid)
            //.restaurantId(restaurantIdValid)
            //.price(BigDecimal.valueOf(33_76))
            //.items(List.of(this.createOrderItemMock()))
            //.address(this.createOrderAddressMock())
            .build();

    this.producerTemplate.sendBody("direct:checkCustomerCommandHandler", body);
    //
    //var mock = context.getEndpoint("mock:result", MockEndpoint.class);
    // asserting mock is satisfied
    mock.assertIsSatisfied();

  }

  @Test
  @Disabled
  public void checkRestaurant() {

    var price = BigDecimal.valueOf(33.76);
    var restaurantIdValid = UUID.fromString("c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b");

    var body = CreateOrderCommandDTO.builder()
            //.customerId(customerIdValid)
            .restaurantId(restaurantIdValid)
            //.price(BigDecimal.valueOf(33_76))
            .items(List.of(this.createOrderItemMock(price)))
            //.address(this.createOrderAddressMock())
            .build();

    this.producerTemplate.sendBody("direct:checkRestaurantCommandHandler", body);

  }

  @Test
  public void createOrder() {

    var price = BigDecimal.valueOf(33.76);
    var customerIdValid = UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9");
    var restaurantIdValid = UUID.fromString("c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b");


    var body = CreateOrderCommandDTO.builder()
            .customerId(customerIdValid)
            .restaurantId(restaurantIdValid)
            .price(price)
            .items(List.of(this.createOrderItemMock(price)))
            .address(this.createOrderAddressMock())
            .build();


    this.producerTemplate.sendBody("direct:createOrderCommandHandler", body);

  }


}
