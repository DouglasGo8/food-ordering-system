package com.food.ordering.system.payment.service.dataaccess;

import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRepository extends RouteBuilder {


  // Payment save(Payment payment)
  @Override
  public void configure() {

    from("direct:savePayment").routeId("SavePaymentRouteId")
            .transform(variable("payment"))
            //.log("Save Payment ---> ${body.createdAt}")
            .to("sql-stored:classpath:templates/insertPayment.sql")
            //.log("${body.price}")
            //.transform(constant("Save the Payment"))
            .end();


    from("direct:updatePayment").routeId("UpdatePaymentRouteId")
            .transform(variable("payment"))
            //.log("Save Payment ---> ${body.createdAt}")
            .to("sql-stored:classpath:templates/updatePayment.sql")
            //.log("${body.price}")
            //.transform(constant("Save the Payment"))
            .end();


    from("direct:paymentFindOrderId").routeId("PaymentFindOrderIdRouteId")
            .setVariable("orderId", simple("${body.orderId}"))
            .to("sql-stored:classpath:templates/findPaymentByOrderId_fn.sql?function=true") // done
            //.log(LoggingLevel.INFO, "${body.orderId}")
            .choice()
              .when(simple("${body['#result-set-1'].size} == 0"))
                .log(LoggingLevel.WARN, DomainConstants.PAYMENT_ORDER_ID_NOT_FOUND)
                .throwException(new OrderDomainException("Payment with OrderId: {} could not be found!"))
            //.otherwise()
            //.log(LoggingLevel.INFO, "Found CustomerId")

            .end();



  }
}
