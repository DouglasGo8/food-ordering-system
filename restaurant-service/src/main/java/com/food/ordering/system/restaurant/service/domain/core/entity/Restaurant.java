package com.food.ordering.system.restaurant.service.domain.core.entity;

import com.food.ordering.system.restaurant.service.domain.core.valueobject.OrderApprovalId;
import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

  private boolean active;
  private OrderApproval orderApproval;
  private final OrderDetail orderDetail;

  private Restaurant(Builder builder) {
    super.setId(builder.restaurantId);
    active = builder.active;
    orderApproval = builder.orderApproval;
    orderDetail = builder.orderDetail;
  }

  public void validateOrder(List<String> failureMessages) {

    if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
      failureMessages.add("Payment isn't completed for order " + orderDetail.getId());
    }

    var totalAmount = orderDetail.getProducts().stream()
            .map(p -> {
              if (!p.isAvailable()) {
                failureMessages.add("Product with id: " + p.getId().getValue() + " is not available!");
              }
              return p.getPrice().multiplyMoney(p.getQuantity());
            })
            .reduce(Money.ZERO, Money::addMoney);

    log.info("{}->{}", totalAmount.getAmount(), orderDetail.getTotalAmount().getAmount());

    if (!totalAmount.equals(orderDetail.getTotalAmount())) {
      log.info("Price total in not correct from order!!!!");
      failureMessages.add("Price total in not correct from order: " + orderApproval.getId());
    }
  }

  public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
    this.orderApproval = OrderApproval.Builder.builder()
            .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
            .restaurantId(this.getId())
            .orderId(this.getOrderDetail().getId())
            .orderApprovalStatus(orderApprovalStatus)
            .build();
  }

  public void setActive(boolean active) {
    this.active = active;
  }


  public static final class Builder {
    private RestaurantId restaurantId;
    private boolean active;
    private OrderApproval orderApproval;
    private OrderDetail orderDetail;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder restaurantId(RestaurantId restaurantId) {
      this.restaurantId = restaurantId;
      return this;
    }

    public Builder active(boolean val) {
      active = val;
      return this;
    }

    public Builder orderApproval(OrderApproval val) {
      orderApproval = val;
      return this;
    }

    public Builder orderDetail(OrderDetail val) {
      orderDetail = val;
      return this;
    }

    public Restaurant build() {
      return new Restaurant(this);
    }
  }
}
