package com.food.ordering.system.shared.domain;

import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AppTest {

  @Test
  public void baseEntityRepresentation() {

    final class MyEntity extends BaseEntity<UUID> {
    }
    //
    var uuid = UUID.randomUUID();
    var myEntity = new MyEntity();
    //
    myEntity.setId(uuid);
    assertNotNull(myEntity.getId());

  }

  @Test
  public void aggregateRootRepresentation() {

    final class MyAggregate extends AggregateRoot<UUID> {
    }
    //
    var uuid = UUID.randomUUID();
    var myEntity = new MyAggregate();
    // Re-use BaseEntity Getters/Setters
    myEntity.setId(uuid);
    assertNotNull(myEntity.getId());

  }

  @Test
  public void baseIdRepresentation() {
    var uuid = UUID.randomUUID();
    var orderId = new OrderId(uuid);
    var productId = new ProductId(uuid);
    var restaurantId = new RestaurantId(uuid);
    //
    assertNotNull(orderId.getValue());
    assertNotNull(productId.getValue());
    assertNotNull(restaurantId.getValue());

  }

  @Test
  public void moneyIsGreaterThanZero() {
    var money1 = new Money(BigDecimal.valueOf(10L)); // usd 10
    var money2 = new Money(BigDecimal.ZERO);
    //
    assertTrue(money1.isGreaterThanZero());
    assertFalse(money2.isGreaterThanZero());
  }

  @Test
  public void moneyIsGreaterThan() {
    var money1 = new Money(BigDecimal.valueOf(10l)); // usd 10
    var money2 = new Money(BigDecimal.valueOf(2l)); // usd 2
    //
    assertTrue(money1.isGreaterThan(money2));
    assertFalse(money2.isGreaterThan(money1));
  }

  @Test
  public void addMoney() {
    var money1 = new Money(BigDecimal.valueOf(10l)); // usd 10
    var money2 = money1.addMoney(money1);

    assertEquals(money2.getAmount(), BigDecimal.valueOf(20l).setScale(2));
    // Ensuring @equals and @hashCode on Records
    assertNotEquals(money1, money2);
  }

  @Test
  public void subtractMoney() {
    var money1 = new Money(BigDecimal.valueOf(10l)); // usd 10
    var money2 = money1.subtractMoney(new Money(BigDecimal.valueOf(2)));

    assertEquals(money2.getAmount(), BigDecimal.valueOf(8l).setScale(2));
    // Ensuring @equals and @hashCode on Records
    assertNotEquals(money1, money2);
  }

  @Test
  public void multiplyMoney() {
    var money1 = new Money(BigDecimal.valueOf(10l)); // usd 10
    var money2 = money1.multiplyMoney(2);

    assertEquals(money2.getAmount(), BigDecimal.valueOf(20l).setScale(2));
    // Ensuring @equals and @hashCode on Records
    assertNotEquals(money1, money2);
  }

  @Test
  public void setScale() {
    var money1 = new Money(BigDecimal.valueOf(10L)); // usd 10
    assertEquals(money1.setScale(money1.getAmount()), BigDecimal.valueOf(10)
            .setScale(2, RoundingMode.HALF_EVEN));
  }
}
