package com.food.ordering.system.payment.service.domain.application;

import com.food.ordering.system.payment.service.domain.application.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainServiceImpl;
import com.food.ordering.system.shared.domain.DomainConstants;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.concurrent.Executors;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestHelper extends RouteBuilder {


  ////
  //// persistPayment @Transactional /PaymentRequest/
  ////
  @Override
  public void configure() {

    // lesson 48
    from("direct:persistPayment").routeId("PersistPaymentRouter")
      .log(LoggingLevel.INFO, DomainConstants.PAYMENT_REQUEST_RECEIVED)
      .setProperty("payment", method(PaymentDataMapper.class))
      // String.class only test purpose
      .multicast(AggregationStrategies.flexible().accumulateInCollection(ArrayList.class)).streaming()
            //.parallelAggregate().parallelProcessing()
            //.executorService(Executors.newFixedThreadPool(3))
            // [0]CreditEntry [1]List<CreditHistory>
        .to("direct:creditEntryFindByCustomerId", "direct:creditHistoryFindByCustomerId")
      .end() // end Multicast
      //.log(LoggingLevel.INFO, "${body}")
      .setProperty("creditEntry",
              method(PaymentDomainServiceImpl.class, "convertListCreditEntriesToCreditEntry(${body[0]['#result-set-1']})"))
      .setProperty("creditHistories",
              method(PaymentDomainServiceImpl.class, "convertListCreditHistoriesJdbcToListCreditHistories(${body[1]['#result-set-1']})"))
      //.setProperty("creditEntry", simple("${body[0]['#result-set-1']}"))
      //.log("${exchangeProperty.creditEntry.customerId.value}")
      //.setProperty("creditHistories", simple("${body[1]['#result-set-1']}"))
      //.bean(PaymentDomainService.class, "validateAndInitializePayment") // PaymentEvent
      //.log("${body}")
  //    .setProperty("paymentEvent", body())
      //.transform(exchangeProperty("payment"))
      //.to("direct:savePayment")
      //.log("${body}")
      // failures.isEmpty choice
      // PK Problem section 06 lesson 48 time 7:00
      //.multicast().parallelProcessing().executorService(Executors.newFixedThreadPool(3))
      //  .to("direct:saveCreditEntry"/*, "direct:saveCreditHistories"*/)
      //.end()
      //
      //
      //.log(LoggingLevel.INFO, "${exchangeProperty.creditHistory}")
      //.log(LoggingLevel.INFO, "Body here ${body[0]['#result-set-1'][0]['id']}")
      // return PaymentEvent
    .end();

    //Section 06 lesson 48 time 8:30
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
