package com.food.ordering.system.restaurant.service.domain.application.mapper;

import com.food.ordering.system.restaurant.service.domain.core.entity.Restaurant;
import java.util.List;

public record ValidateOrderMapper(Restaurant restaurant, List<String> failures) {
}
