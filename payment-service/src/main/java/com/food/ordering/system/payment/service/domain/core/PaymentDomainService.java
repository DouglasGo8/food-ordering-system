package com.food.ordering.system.payment.service.domain.core;

import com.food.ordering.system.payment.service.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {
  PaymentEvent validateAndInitializePayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories,
                                            List<String> failureMessages);

  PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories,
                                        List<String> failureMessages);
}
