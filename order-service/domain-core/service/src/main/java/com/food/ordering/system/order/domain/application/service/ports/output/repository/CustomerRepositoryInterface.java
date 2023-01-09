package com.food.ordering.system.order.domain.application.service.ports.output.repository;

import com.food.ordering.system.order.domain.core.service.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryInterface {
  Optional<Customer> findCustomer(UUID customerId);
}
