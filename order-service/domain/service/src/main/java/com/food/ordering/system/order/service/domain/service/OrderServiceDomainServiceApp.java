package com.food.ordering.system.order.service.domain.service;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.camel.quarkus.main.CamelMainApplication;

@QuarkusMain
public class OrderServiceDomainServiceApp {
  public static void main(String... args) {
    Quarkus.run(CamelMainApplication.class, args);
  }
}
