package com.food.ordering.system.order.service;

import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  //@Inject
  //OrderDataMapper orderDataMapper;

  //@Inject
  //CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;

  @Test
  @SneakyThrows
  public void createOrderCommandDTOByOrderControllerRepresentation() {
    AdviceWith.adviceWith(super.context, "CreateOrderCMDH", r -> r.weaveAddLast().to("mock:result"));
    var body = this.createOrderCommandDTOFullMock();
    var mock = super.getMockEndpoint("mock:result");
    this.producerTemplate.sendBody("direct:createOrderCommandHandler", body);
    //log.info("{}", body.getCustomerId());
    mock.setExpectedMessageCount(1);
    //
    //assertMockEndpointsSatisfied();
    mock.assertIsSatisfied();
  }

  @Test
  @Disabled
  @SneakyThrows
  public void createOrderCommandDTOByOrderControllerRepresentationWithInvalidCustomerId() {
    //AdviceWith.adviceWith(super.context, "CreateOrderCMDH", r ->
    //        r.weaveAddFirst().to("mock:result"));
    var threwException = false;
    var body = this.createOrderCommandDTOFullMockWithInvalidUUID();
    //var mock = super.getMockEndpoint("mock:result");
    //mock.expectedMessageCount(1);
    try {
      this.producerTemplate.sendBody("direct:createOrderCommandHandler", body);
    } catch (OrderDomainException e) {
      log.info("Exception here");
      threwException = true;
      //var cee = assertIsInstanceOf(CamelExecutionException.class, e);
      //var cause = cee.getCause();
      //assertIsInstanceOf(DomainException.class, cause);
    }

    //assertTrue(threwException);
    //assertMockEndpointsSatisfied();

  }


  @Test
  @Disabled
  @SneakyThrows
  public void createOrderMessagingToOrderCreatedEvent() {

    AdviceWith.adviceWith(context, "PublishOrderCreatedPayment",
            r -> r.weaveAddLast().to("mock:result"));
    //
    var order = this.initializerToValidateOrderInitiateMock();
    var body = new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    //
    var mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    this.producerTemplate.sendBody("seda:publishOrderCreatedPayment", body);
    mock.assertIsSatisfied();
    // PaymentStatus
    assertTrue(mock.getReceivedExchanges().get(0).getIn().getBody().toString().contains("PENDING"));
  }


  /*@Test
  @Disabled
  @SneakyThrows
  public void createOrderMessagingToOrderCancelledEvent() {

    //AdviceWith.adviceWith(super.context, "OrderMessagingProducerHandler",
    //        r -> r.weaveAddLast().to("mock:result"));
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
  }*/


}