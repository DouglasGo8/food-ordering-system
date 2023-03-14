package com.food.ordering.system.order.service.application;


import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;

import javax.inject.Inject;

@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport {

  @Inject
  CamelContext context;

  @Inject
  ProducerTemplate producerTemplate;


}
