package com.food.ordering.system.payment.service.domain.application;

import com.food.ordering.system.payment.service.domain.application.dto.CreditAggregation;
import com.food.ordering.system.payment.service.domain.application.dto.CreditEntryFromDb;
import com.food.ordering.system.payment.service.domain.application.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainServiceImpl;
import com.food.ordering.system.shared.domain.DomainConstants;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestHelper extends RouteBuilder {

  ////
  //// persistPayment @Transactional /PaymentRequest/
  ////
  @Override
  public void configure() {

    from("direct:persistPayment").routeId("PersistPaymentRouter")
      .log(LoggingLevel.INFO, DomainConstants.PAYMENT_REQUEST_RECEIVED)
      .setProperty("payment", method(PaymentDataMapper.class, "paymentRequestModelToPayment"))
      // String.class only test purpose
      // refactory to re-use
      .multicast(AggregationStrategies.flexible().accumulateInCollection(ArrayList.class)).streaming()
            //.parallelAggregate().parallelProcessing()
            //.executorService(Executors.newFixedThreadPool(3))
            // [0]CreditEntry [1]List<CreditHistory>
        .to("direct:creditEntryFindByCustomerId", "direct:creditHistoryFindByCustomerId")
      .end() // end Multicast
      //.log(LoggingLevel.INFO, "${body}")
      .setProperty("creditEntries", simple("${body[0]['#result-set-1']}"))
      .setProperty("creditHistories", simple("${body[1]['#result-set-1']}"))
      .bean(PaymentDomainService.class, "validateAndInitializePayment") // PaymentEvent

      //.to("direct:savePayment")
       //     .log("${body}")
      // failures.isEmpty
      //.to("direct:saveCreditEntry")
      //.to("direct:saveCreditHistories")
      //.log(LoggingLevel.INFO, "${exchangeProperty.creditHistory}")
      //.log(LoggingLevel.INFO, "Body here ${body[0]['#result-set-1'][0]['id']}")
      // return PaymentEvent
    .end();

    //// persistCancelCamel @Transactional /PaymentRequest/
    from("direct:persistCancelPayment").routeId("PersistCancelPaymentRouter")
      .log(LoggingLevel.INFO, DomainConstants.PAYMENT_ROLLBACK_RECEIVED)
      //.to("direct:paymentFindOrderId") // payment
      // refactory to re-use
      .multicast(AggregationStrategies.flexible().accumulateInCollection(ArrayList.class)).streaming()
            //.parallelAggregate().parallelProcessing()
            //.executorService(Executors.newFixedThreadPool(3))
            // [0]CreditEntry [1]List<CreditHistory>
            .to("direct:creditEntryFindByCustomerId", "direct:creditHistoryFindByCustomerId")
      .end() // end Multicast
      //.bean(PaymentDomainServiceImpl.class, "validateAndCancelPayment") // PaymentEvent
      .log(LoggingLevel.INFO, "${body}")
            // failures.isEmpty
            //.to("direct:saveCreditEntry")
            //.to("direct:saveCreditHistories")
            // return PaymentEvent
      .end();


  }
}
