package com.food.ordering.system.restaurant.service;

import com.food.ordering.system.restaurant.service.domain.application.mapper.ValidateOrderMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@Slf4j
@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  ProducerTemplate producerTemplate;

  @Test
  @Disabled
  public void validateOrderRouteRepresentation() {
    var failures = new ArrayList<String>();
    var body = new ValidateOrderMapper(this.restaurantMock(), failures);
    //
    this.producerTemplate.sendBody("direct:validateOrder", body);

  }

  @Test
  public void persistOrderApprovalRepresentation() {
    //
  }

}
