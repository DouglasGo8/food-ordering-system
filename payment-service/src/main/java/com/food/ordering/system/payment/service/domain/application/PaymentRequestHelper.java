package com.food.ordering.system.payment.service.domain.application;

import com.food.ordering.system.payment.service.domain.application.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainServiceImpl;
import com.food.ordering.system.shared.domain.DomainConstants;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestHelper extends RouteBuilder {


  // persistPayment
  @Override
  public void configure() {
    from("direct:paymentRequestHelper").routeId("PaymentRequestHelperRouter")
      .log(LoggingLevel.INFO, DomainConstants.PAYMENT_REQUEST_RECEIVED)
      .setProperty("payment", method(PaymentDataMapper.class, "paymentRequestModelToPayment"))
      // String.class only test purpose
      .multicast(AggregationStrategies.flexible().accumulateInCollection(ArrayList.class)).streaming()
            //.parallelAggregate().parallelProcessing()
            //.executorService(Executors.newFixedThreadPool(3))
            // [0]CreditEntry [1]CreditHistory
        .to("direct:creditEntryRepository", "direct:creditHistoryRepository")
      .end() // end Multicast
      //.bean(PaymentDomainServiceImpl.class, "validateAndInitializePayment") // PaymentEvent
      .log(LoggingLevel.INFO, "${body}");

  }
}
