package com.food.ordering.system.restaurant.service.domain.application.mapper;

import com.food.ordering.system.restaurant.service.domain.core.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor
public class RestaurantDataAccessMapper {

  public String joinedProductIds(List<Product> products) {
    return products.stream()
            .map(p -> p.getId().getValue().toString())
            //.map(Objects::toString)
            .collect(Collectors.joining(", "));
  }
}
