package com.food.ordering.system.restaurant.service.domain.application.mapper;


import com.food.ordering.system.restaurant.service.domain.core.entity.Restaurant;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Variable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor
public class RestaurantRepoMapper {

  @Handler
  public Restaurant updateRestaurantInfo(@Variable("orderId") String orderId,
                                         @Variable("restaurant") Restaurant restaurant,
                                         @Body ArrayList<Map<String, Object>> restaurantAndProducts) {
    //
    restaurant.setActive(Boolean.parseBoolean(restaurantAndProducts.getFirst().get("restaurant_active").toString()));
    restaurant.getOrderDetail().getProducts()
            .forEach(p -> restaurantAndProducts.forEach(repo -> {
              if (p.getId().getValue().toString().equals(repo.get("product_id").toString())) {
                p.updateWithConfirmedPriceAndAvailable(
                        repo.get("restaurant_name").toString(),
                        new Money(BigDecimal.valueOf(Double.parseDouble(repo.get("product_price").toString()))),
                        Boolean.parseBoolean(repo.get("product_available").toString())
                );
              }
            }));
    restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(orderId)));
    //
    return restaurant;
  }
}
