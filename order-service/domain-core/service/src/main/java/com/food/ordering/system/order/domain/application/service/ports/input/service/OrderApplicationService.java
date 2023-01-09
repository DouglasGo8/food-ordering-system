package com.food.ordering.system.order.domain.application.service.ports.input.service;

import com.food.ordering.system.order.domain.application.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.application.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.domain.application.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.domain.application.service.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {
  TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
  CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
}
