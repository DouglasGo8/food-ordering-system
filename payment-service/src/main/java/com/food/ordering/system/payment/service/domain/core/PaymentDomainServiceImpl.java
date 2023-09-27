package com.food.ordering.system.payment.service.domain.core;

import com.food.ordering.system.payment.service.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditEntryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangeProperty;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class PaymentDomainServiceImpl implements PaymentDomainService {

  @Override
  public PaymentEvent validateAndInitializePayment(@ExchangeProperty("payment") Payment payment,
                                                   @ExchangeProperty("creditEntries") ArrayList<Map<String, Object>> creditEntries,
                                                   @ExchangeProperty("creditHistories") ArrayList<Map<String, Object>> creditHistories,
                                                   List<String> failureMessages) {
    //
    var newCreditEntry = this.convertListCreditEntriesToCreditEntry(creditEntries);
    //
    var newCreditHistories = this.convertListCreditHistoriesJdbcToListCreditHistories(creditHistories);
    //
    payment.validatePayment(failureMessages);
    payment.initializePayment();
    //
    this.validateCreditEntry(payment, newCreditEntry, failureMessages);
    this.subtractCreditEntry(payment, newCreditEntry);
    // fixed java.lang.UnsupportedOperationException
    this.updateCreditHistory(payment, newCreditHistories, TransactionType.DEBIT);
    this.validateCreditHistory(newCreditEntry, newCreditHistories, failureMessages);
    //
    if (failureMessages.isEmpty()) {
      log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.COMPLETED);
      return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    } else {
      log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.FAILED);
      return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)), failureMessages);
    }
  }

  @Override
  public PaymentEvent validateAndCancelPayment(Payment payment, ArrayList<Map<String, Object>> creditEntries,
                                               ArrayList<Map<String, Object>> creditHistories, List<String> failureMessages) {
    payment.validatePayment(failureMessages);
    //
    var newCreditEntry = this.convertListCreditEntriesToCreditEntry(creditEntries);
    var newCreditHistories = this.convertListCreditHistoriesJdbcToListCreditHistories(creditHistories);

    this.addCreditEntry(payment, newCreditEntry);
    this.updateCreditHistory(payment, newCreditHistories, TransactionType.CREDIT);

    if (failureMessages.isEmpty()) {
      log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.CANCELLED);
      // Change UTC value to shared domain constant
      return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    } else {
      log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.FAILED);
      return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)), failureMessages);
    }
  }

  private CreditEntry convertListCreditEntriesToCreditEntry(ArrayList<Map<String, Object>> creditEntries) {
    return CreditEntry.builder()
            .creditEntryId(new CreditEntryId(UUID.fromString(creditEntries.get(0).get("id").toString())))
            .customerId(new CustomerId(UUID.fromString(creditEntries.get(0).get("customer_id").toString())))
            //.totalCreditAmount(new Money(BigDecimal.valueOf(Double.parseDouble(NumberFormat.getInstance()
            //        .format(creditEntries.get(0).get("total_credit_amount"))))))
            .totalCreditAmount(new Money((BigDecimal) creditEntries.get(0).get("total_credit_amount")))
            //.totalCreditAmount(new Money(new BigDecimal(creditEntries.get(0).get("total_credit_amount"))))
            .build();
  }

  private List<CreditHistory> convertListCreditHistoriesJdbcToListCreditHistories(ArrayList<Map<String, Object>> creditHistories) {
    return new ArrayList<>(creditHistories.stream()
            .flatMap(map -> Stream.of(CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(UUID.fromString(map.get("id").toString())))
                    .customerId(new CustomerId(UUID.fromString(map.get("customer_id").toString())))
                    .amount(new Money((BigDecimal) map.get("amount")))
                    .transactionType(TransactionType.valueOf(map.get("type").toString()))
                    //.amount(new Money(BigDecimal.valueOf(Double.parseDouble(NumberFormat.getInstance().format(map.get("amount"))))))
                    //        .amount(new Money(new BigDecimal(map.get("amount"))))
                    .build()))
            .toList());
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
            .transactionType(transactionType)
            .build();
    //

    creditHistories.add(creditHistory);
  }

  private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories,
                                     List<String> failureMessages) {

    var totalCreditHistory = this.getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
    var totalDebitHistory = this.getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

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
