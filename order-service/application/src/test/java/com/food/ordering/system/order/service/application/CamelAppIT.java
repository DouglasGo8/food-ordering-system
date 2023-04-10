package com.food.ordering.system.order.service.application;


import com.food.ordering.system.order.service.application.mediation.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.core.event.OrderCancelledEvent;
import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  OrderDataMapper orderDataMapper;

  @Inject
  CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;


  @Test
  @Disabled
  public void createOrderCommandDTOByOrderControllerRepresentation() {
    var body = this.createOrderCommandDTOFullMock();
    this.producerTemplate.sendBody("direct:createOrderCommandHandler", body);
    //log.info("{}", body.getCustomerId());
  }

  @Test
  @Disabled
  @SneakyThrows
  public void createOrderMessagingToOrderCreatedEvent() {

    AdviceWith.adviceWith(context, "OrderMessagingProducerHandler",
            r -> r.weaveAddLast().to("mock:result"));
    //
    var orderDTO = this.createOrderCommandDTOFullMock();
    var order = this.orderDataMapper.createOrderCommandToOrder(orderDTO);
    var restaurant = this.orderDataMapper.createOrderCommandToRestaurant(orderDTO);
    var body = this.orderDataMapper.validateAndInitializeOrder(order, restaurant);
    //
    var mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    this.producerTemplate.sendBody("direct:orderMessagingProducerHandler", body);
    assertMockEndpointsSatisfied();
    // PaymentStatus
    assertTrue(mock.getReceivedExchanges().get(0).getIn().getBody().toString().contains("PENDING"));
  }


  @Test
  @SneakyThrows
  public void createOrderMessagingToOrderCancelledEvent() {

    AdviceWith.adviceWith(context, "OrderMessagingProducerHandler",
            r -> r.weaveAddLast().to("mock:result"));
    //
    var orderDTO = this.createOrderCommandDTOFullMock();
    var order = this.orderDataMapper.createOrderCommandToOrder(orderDTO);
    var restaurant = this.orderDataMapper.createOrderCommandToRestaurant(orderDTO);
    this.orderDataMapper.validateAndInitializeOrder(order, restaurant); // forces OrderCreatedEvent
    var body = new OrderCancelledEvent(order, ZonedDateTime.now()); //this.orderDataMapper.validateAndInitializeOrder(order, restaurant);?
    //log.info(body.getOrder().getId().getValue().toString());
    //
    var mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    this.producerTemplate.sendBody("direct:orderMessagingProducerHandler", body);
    assertMockEndpointsSatisfied();
    // PaymentStatus
    assertTrue(mock.getReceivedExchanges().get(0).getIn().getBody().toString().contains("CANCELLED"));
  }

  @Test
  @SneakyThrows
  public void createRestaurantMessagingToOrderCancelledEvent() {

    //var body = new OrderPaidEvent();
  }

}
