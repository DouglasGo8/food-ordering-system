package com.food.ordering.system.order.domain.application.service.ports.output.repository;

import com.food.ordering.system.order.domain.core.service.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
  Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}
