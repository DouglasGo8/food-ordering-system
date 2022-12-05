package com.food.ordering.system.common.domain.test;

import com.food.ordering.system.common.domain.entity.AggregateRoot;
import com.food.ordering.system.common.domain.valueobject.CustomerId;
import com.food.ordering.system.common.domain.valueobject.Money;
import com.food.ordering.system.common.domain.valueobject.OrderId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AppTest {

  @Test
  public void getIdFromAggregateRoot() {
    class OrderProcessing extends AggregateRoot<String> {
    }
    //
    var op = new OrderProcessing();
    op.setId(UUID.randomUUID().toString());

    assertNotNull(op.getId());

  }

  @Test
  public void getValueFromOrderId() {
    var orderId = new OrderId(UUID.randomUUID());
    log.info("Id {}", orderId.getValue());
    assertNotNull(orderId);
  }

  @Test
  public void getAmountFromMoney() {
    var money = new Money(BigDecimal.valueOf(10L));
    log.info("$ {}", money.amount());
    assertEquals(BigDecimal.valueOf(10), money.amount());
  }

  @Test
  public void isAmountGreaterThanZero() {
    var money = new Money(BigDecimal.valueOf(10L));
    assertTrue(money.isGreaterThanZero());
    assertFalse(money.isGreaterThan(money));
  }

  @Test
  public void addMoney() {
    var money = new Money(BigDecimal.valueOf(0L));
    var m2 = money.add(new Money(BigDecimal.valueOf(10L)));
    log.info("$ {}", m2.amount());
    assertEquals("10.00", m2.amount().toString());
    assertNotEquals(m2.hashCode(), money.hashCode());
  }

  @Test
  public void subtractMoney() {
    var money = new Money(BigDecimal.valueOf(10L));
    var m2 = money.subtract(new Money(BigDecimal.valueOf(5L)));
    log.info("$ {}", m2.amount());
    assertEquals("5.00", m2.amount().toString());
    assertNotEquals(m2.hashCode(), money.hashCode());
  }

  @Test
  public void multiplyMoney() {
    var money = new Money(BigDecimal.valueOf(10L));
    var m2 = money.multiply(new Money(BigDecimal.valueOf(2L)));
    log.info("$ {}", m2.amount());
    assertEquals("20.00", m2.amount().toString());
    assertNotEquals(m2.hashCode(), money.hashCode());
  }

  @Test
  public void addScale() {
    var money = new Money(BigDecimal.valueOf(10L));
    assertEquals(money.setScale(money.amount()).toString(), "10.00");
  }

  @Test
  public void createValueObject() {
    final class Customer {
      CustomerId customerId;
    }

    var customer = new Customer();

    customer.customerId = new CustomerId(UUID.randomUUID());

    assertNotNull(customer.customerId.getValue());

  }
}
