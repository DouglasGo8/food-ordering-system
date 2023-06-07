package com.food.ordering.system.customer.service.rest;


import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class CustomerController extends RouteBuilder {
  @Override
  public void configure() {
    //
    restConfiguration().component("netty-http")
            // ---------- CONTEXT PATH BEGIN ---------------------
            .contextPath("/customer")
            // ---------- CONTEXT PATH END -----------------------
            .dataFormatProperty("prettyPrint", "true")
            .host("0.0.0.0").port(12081).bindingMode(RestBindingMode.auto);



  }
}
