package com.food.ordering.system.shared.order.core.service.domain.valueobject;

import java.util.UUID;

public record StreetAddress(UUID id, String city, String street, String postalCode) {
}