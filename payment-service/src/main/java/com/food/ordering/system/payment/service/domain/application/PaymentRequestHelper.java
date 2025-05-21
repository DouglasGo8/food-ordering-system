package com.food.ordering.system.payment.service.domain.application;

import com.food.ordering.system.payment.service.domain.application.mapper.PaymentRequestDataMapper;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainServiceImpl;
import com.food.ordering.system.shared.domain.DomainConstants;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.concurrent.Executors;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestHelper extends RouteBuilder {

  //final String transformToPayment = ""; ok
  //final String transformToCreditEntryOnPersist = "convertListCreditEntriesToCreditEntry(${body[0]['#result-set-1']})";
  //final String transformToCreditEntryOnCancel = "convertListCreditEntriesToCreditEntry(${body[1]['#result-set-1']})";

  //final String transformToCreditHistoriesOnPersist = "convertListCreditHistoriesJdbcToListCreditHistories(${body[1]['#result-set-1']})";
  //final String transformToCreditHistoriesOnCancel = "convertListCreditHistoriesJdbcToListCreditHistories(${body[2]['#result-set-1']})";
  ////
  //// persistPayment @Transactional /PaymentRequest/
  ////
  @Override
  public void configure() {

    // lesson 48, done
    from("direct:persistPayment").routeId("PersistPaymentRouteId")
      .log(LoggingLevel.INFO, DomainConstants.PAYMENT_REQUEST_RECEIVED)
      .setVariable("payment", method(PaymentRequestDataMapper.class))
      // String.class only test purpose
      .multicast(AggregationStrategies.flexible().accumulateInCollection(ArrayList.class)).streaming()
            //.parallelAggregate().parallelProcessing()
            //.executorService(Executors.newFixedThreadPool(3))
            // [0]CreditEntry [1]List<CreditHistory>
        .to("direct:creditEntryFindByCustomerId", "direct:creditHistoryFindByCustomerId")
      .end() // end Multicast
      //.log(LoggingLevel.INFO, "${body}")
      .setVariable("creditEntry", method(PaymentDomainServiceImpl.class, "{{spEL.methodCreditEntries}}{{spEL.resultSetOne}}"))
      .setProperty("creditHistories", method(PaymentDomainServiceImpl.class, "{{spEL.methodListCreditHistories}}{{spEL.resultSetTwo}}"))
      //.setProperty("creditEntry", simple("${body[0]['#result-set-1']}"))
      //.log("${exchangeProperty.creditEntry.customerId.value}")
      //.setProperty("creditHistories", simple("${body[1]['#result-set-1']}"))
      .bean(PaymentDomainService.class, "validateAndInitializePayment") // paymentEvent
      //.log("${body}")
      .setVariable("paymentEvent", body())
      //.log("${body}")
      // failures.isEmpty choice
      // PK Problem section 06 lesson 48 time 7:00 solved with Postgres ON CONFLICT approach
      .multicast().parallelProcessing().executorService(Executors.newFixedThreadPool(3))
        .to("direct:savePayment",
                "direct:saveCreditEntry",
                "direct:saveCreditHistories")
      .end() // end Multicast
      .transform(variable("paymentEvent"))
      .log(LoggingLevel.INFO,"${body}")
      // return PaymentEvent
      // async invoke PaymentRequestMessageLister Route
    .end();

    //Section 06 lesson 48 time 8:30
    //// persistCancelCamel @Transactional /PaymentRequest/
    from("direct:persistCancelPayment").routeId("PersistCancelPaymentRouteId")
      .log(LoggingLevel.INFO, DomainConstants.PAYMENT_ROLLBACK_RECEIVED)
      //.setVariable("payload", body())
      //.to("direct:paymentFindOrderId") // paymentByOrderId
      //.setVariable("paymentOrderById", body())
      //.transform(simple("${variable.payload}"))
      // refactor to re-use
      .multicast(AggregationStrategies.flexible().accumulateInCollection(ArrayList.class)).streaming()
            //.parallelAggregate().parallelProcessing()
            //.executorService(Executors.newFixedThreadPool(3))
            // [0]Payment [1]CreditEntry [1]List<CreditHistory>
           .to("direct:paymentFindOrderId",
                   "direct:creditEntryFindByCustomerId",
                   "direct:creditHistoryFindByCustomerId")
      .end() // end Multicast
      .setVariable("payment", method(PaymentDomainServiceImpl.class, "{{spEL.methodPaymentList}}{{spEL.resultSetOne}}"))
      .setVariable("creditEntry", method(PaymentDomainServiceImpl.class, "{{spEL.methodCreditEntries}}{{spEL.resultSetTwo}}"))
      .setProperty("creditHistories", method(PaymentDomainServiceImpl.class, "{{spEL.methodListCreditHistories}}{{spEL.resultSetThree}}"))
      //
      .bean(PaymentDomainServiceImpl.class, "validateAndCancelPayment") // paymentEvent
      .setVariable("paymentEvent", body())
      .log(LoggingLevel.INFO, "${body}")
            // failures.isEmpty
      .multicast().parallelProcessing()
          .executorService(Executors.newFixedThreadPool(3))
          .to("direct:updatePayment", "direct:saveCreditEntry", "direct:saveCreditHistories")
      .end()// end Multicast
      .transform(variable("paymentEvent"))
       // async invoke PaymentRequestMessageLister Route
    .end();


  }
}
