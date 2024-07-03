package com.food.ordering.system.order.service.domain.application.mapper;

import com.food.ordering.system.order.service.domain.application.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.application.dto.create.RestaurantProductsInfoDTO;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.component.sql.ResultSetIterator;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class RestaurantProductsMapper {

  public IntrospectHeaderIdsMapper restaurantProductsIdsSQLClause(@ExchangeProperty("restaurant") Restaurant restaurant) {
    return new IntrospectHeaderIdsMapper(restaurant.getId().getValue().toString(),
            restaurant.getProducts().stream().map(p -> p.getId().getValue().toString())
                    .collect(Collectors.joining(",")));
  }

  public Restaurant resultSetIteratorToRestaurant(@Body ResultSetIterator body,
                                                  @ExchangeProperty("payload") CreateOrderCommandDTO createOrderCommandDTO) {

    var list = new ArrayList<RestaurantProductsInfoDTO>();
    //
    if (!body.hasNext()) {
      var message = String.format(DomainConstants.RESTAURANT_INFO_NOT_FOUND, createOrderCommandDTO.getRestaurantId());
      log.warn(message);
      throw new OrderDomainException(message);
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
