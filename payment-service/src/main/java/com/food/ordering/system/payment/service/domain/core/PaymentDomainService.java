package com.food.ordering.system.payment.service.domain.core;

import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.event.PaymentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface PaymentDomainService {
  PaymentEvent validateAndInitializePayment(Payment payment, ArrayList<Map<String, Object>> creditEntries,
                                            ArrayList<Map<String, Object>> creditHistories,
                                            List<String> failureMessages);

  PaymentEvent validateAndCancelPayment(Payment payment, ArrayList<Map<String, Object>> creditEntry,
                                        ArrayList<Map<String, Object>> creditHistories,
                                        List<String> failureMessages);
}
