package com.food.ordering.system.order.service.application.mediation.mapper;

import com.food.ordering.system.order.service.application.mediation.bean.RestaurantAndProductsInfo;
import com.food.ordering.system.order.service.application.mediation.dto.create.RestaurantProductsInfoDTO;
import com.food.ordering.system.order.service.application.mediation.dto.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.component.sql.ResultSetIterator;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;


@NoArgsConstructor
@ApplicationScoped
public class RestaurantProductsInMapper {

  public RestaurantAndProductsInfo restaurantAndProductsInfoInClause(@ExchangeProperty("restaurant") Restaurant restaurant) {
    return new RestaurantAndProductsInfo(restaurant.getId().getValue().toString(),
            restaurant.getProducts().stream().map(p -> p.getId().getValue().toString())
                    .collect(Collectors.joining(",")));
  }

  public Restaurant resultSetIteratorToRestaurant(@Body ResultSetIterator body) {

    var list = new ArrayList<RestaurantProductsInfoDTO>();

    if (!body.hasNext()) {
      throw new RestaurantDataAccessException("Restaurant could not be found!");
    }

    while (body.hasNext()) {
      list.add((RestaurantProductsInfoDTO) body.next());
    }

    var restaurant = list.stream().findFirst().orElseThrow();
    var restaurantId = new RestaurantId(UUID.fromString(restaurant.getRestaurant_id()));
    var restaurantIsActive = restaurant.getRestaurant_active();

    var products = list.stream().map(e -> new Product(new ProductId(UUID.fromString(e.getProduct_id())),
            e.getProduct_name(), new Money(e.getProduct_price()))).collect(Collectors.toList()); // List<Product>

    return new Restaurant(restaurantId, restaurantIsActive, products);
  }
}
