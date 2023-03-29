package com.food.ordering.system.order.service.application;


import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@Slf4j
@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  //@Inject
  //CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;


  @Test
  public void createOrderCommandDTOByOrderControllerRepresentation() {
    var body = this.createOrderCommandDTOFullMock();
    this.producerTemplate.sendBody("direct:createOrderCommandHandler", body);
    //log.info("{}", body.getCustomerId());
  }


}
