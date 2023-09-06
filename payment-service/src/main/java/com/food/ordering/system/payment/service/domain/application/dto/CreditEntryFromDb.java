package com.food.ordering.system.payment.service.domain.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CreditEntryFromDb {
  private String id;
  private String customer_id;
  private long total_credit_amount;
}
