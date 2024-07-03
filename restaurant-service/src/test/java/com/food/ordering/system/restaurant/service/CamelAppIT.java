package com.food.ordering.system.restaurant.service;

import com.food.ordering.system.shared.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.shared.avro.model.RestaurantOrderStatus;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  ProducerTemplate producerTemplate;

  @Test
  @Disabled
  public void restaurantApprovalRequestMessageListenerRepresentation() {
    // RestaurantApprovalRequest

    var body = this.restaurantApprovalRequestMock();

    producerTemplate.sendBody("direct:persistOrderApproval", body);
  }

  @Test
  public void restaurantApprovalRequestAvroModelToRestaurantApproval() {
    var body = RestaurantApprovalRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setRestaurantId("d215b5f8-0249-4dc5-89a3-51fd148cfb45")
            .setOrderId("ec78b161-3899-4866-8753-886b84a8fbce")
            .setCreatedAt(Instant.now())
            .setPrice(BigDecimal.valueOf(177.04))
            .setProducts(this.productAvroMockList())
            .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
            .build();

    producerTemplate.sendBody("direct:approveOrder", body);
  }

}
