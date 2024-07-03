package com.food.ordering.system.order.service.domain.core.valueobject;

import java.util.UUID;

public record StreetAddress(UUID id, String city, String street, String postalCode) {
}