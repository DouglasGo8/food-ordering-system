package com.food.ordering.system.order.service;

import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.shared.domain.DomainConstants;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
  @Disabled
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

  @Test
  @Disabled
  public void paymentResponseKafkaListenerCompletedRepresentation() {
    //
    var body = this.createPaymentResponseCompletedMock();
    this.producerTemplate.sendBody("direct:mockPaymentResponseKafkaListener", body);
  }

  @Test
  @Disabled
  public void paymentResponseKafkaListenerCancelledRepresentation() {
    // move to exception camel test
    var body = this.createPaymentResponseCancelledMock();
    this.producerTemplate.sendBody("direct:mockPaymentResponseKafkaListener", body);
  }

  @Test
  @Disabled
  // orderStatus should be PAID
  public void restaurantApprovalResponseAvroModelApprovedRepresentation() {
    var body = this.createRestaurantApprovalResponseApprovedMock();
    this.producerTemplate.sendBody("direct:mockRestaurantResponseKafkaListener", body);
  }

  @Test
  @Disabled
  public void restaurantApprovalResponseAvroModelRejectedRepresentation() {
    var body = this.createRestaurantApprovalResponseRejectedMock();
    this.producerTemplate.sendBody("direct:mockRestaurantResponseKafkaListener", body);
  }


}