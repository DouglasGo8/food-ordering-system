package com.food.ordering.system.restaurant.service.domain.core.entity;

import com.food.ordering.system.restaurant.service.domain.core.valueobject.OrderApprovalId;
import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class OrderApproval extends BaseEntity<OrderApprovalId> {

  private final OrderId orderId;
  private final RestaurantId restaurantId;
  private final OrderApprovalStatus approvalStatus;

  public OrderApproval(Builder builder) {
    setId(builder.orderApprovalId);
    orderId = builder.orderId;
    restaurantId = builder.restaurantId;
    approvalStatus = builder.orderApprovalStatus;
  }

  public static final class Builder {
    private OrderId orderId;
    private RestaurantId restaurantId;
    private OrderApprovalStatus orderApprovalStatus;
    private OrderApprovalId orderApprovalId;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder orderId(OrderId orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder restaurantId(RestaurantId restaurantId) {
      this.restaurantId = restaurantId;
      return this;
    }

    public Builder orderApprovalStatus(OrderApprovalStatus orderApprovalStatus) {
      this.orderApprovalStatus = orderApprovalStatus;
      return this;
    }

    public Builder orderApprovalId(OrderApprovalId id) {
      this.orderApprovalId = id;
      return this;
    }

    public OrderApproval build() {
      return new OrderApproval(this);
    }
  }
}
