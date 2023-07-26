package com.food.ordering.system.payment.service.domain.core;

import com.food.ordering.system.payment.service.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class PaymentDomainServiceImpl implements PaymentDomainService {
  @Override
  public PaymentEvent validateAndInitializePayment(Payment payment, CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories, List<String> failureMessages) {
    payment.validatePayment(failureMessages);
    payment.initializePayment();
    this.validateCreditEntry(payment, creditEntry, failureMessages);
    this.subtractCreditEntry(payment, creditEntry);
    this.updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
    this.validateCreditHistory(creditEntry, creditHistories, failureMessages);
    //
    if (failureMessages.isEmpty()) {
      log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.COMPLETED);
      return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")));
    } else {
      log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.FAILED);
      return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
    }
  }

  @Override
  public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                               List<CreditHistory> creditHistories, List<String> failureMessages) {
    payment.validatePayment(failureMessages);
    this.addCreditEntry(payment, creditEntry);
    this.updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

    if (failureMessages.isEmpty()) {
      log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.CANCELLED);
      // Change UTC value to shared domain constant
      return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")));
    } else {
      log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.FAILED);
      return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
    }
  }


  private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
    if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
      log.error("Customer with id: {} doesn't have enough credit for payment!", payment.getCustomerId().getValue());
      failureMessages.add("Customer with id " + payment.getCustomerId().getValue() +
              "doesn't have enough credit for payment!");
    }
  }

  private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.subtractCreditAmount(payment.getPrice());
  }

  private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories,
                                   TransactionType transactionType) {

    var amount = payment.getPrice();
    var customerId = payment.getCustomerId();
    var creditHistoryId = new CreditHistoryId(UUID.randomUUID());
    var creditHistory = CreditHistory.builder()
            .amount(amount)
            .customerId(customerId)
            .creditHistoryId(creditHistoryId)
            .build();
    //
    creditHistories.add(creditHistory);
  }

  private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories,
                                     List<String> failureMessages) {

    var totalDebitHistory = this.getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);
    var totalCreditHistory = this.getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
    //
    if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
      log.error("Customer with id: {} doesn't have enough credit according to credit history!",
              creditEntry.getCustomerId().getValue());
      failureMessages.add("Customer with id= " + creditEntry.getCustomerId().getValue() +
              " doesn't have enough credit according to credit history!");
    }
    //
    if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtractMoney(totalDebitHistory))) {
      log.error("Credit history is not equal to current credit from customer id: {}",
              creditEntry.getCustomerId().getValue());
      failureMessages.add("Credit history is not equal to current credit from customer id: " +
              creditEntry.getCustomerId().getValue());
    }

  }

  private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
    return creditHistories.stream()
            .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
            .map(CreditHistory::getAmount)
            .reduce(Money.ZERO, Money::addMoney);
  }


  private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.addCreditAmount(payment.getPrice());
  }


}
