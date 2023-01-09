package com.food.ordering.system.order.domain.core.service.valuobject;

import java.util.UUID;
public record StreetAddress(UUID id, String city, String postalCode, String streetName) {
}
