package com.food.ordering.system.payment.service.domain.core;

import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.event.PaymentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PaymentDomainService {
  PaymentEvent validateAndInitializePayment(Payment payment, ArrayList<Map<String, String>> creditEntries,
                                            ArrayList<Map<String, String>> creditHistories,
                                            List<String> failureMessages);

  PaymentEvent validateAndCancelPayment(Payment payment, ArrayList<Map<String, String>> creditEntry,
                                        ArrayList<Map<String, String>> creditHistories,
                                        List<String> failureMessages);
}
