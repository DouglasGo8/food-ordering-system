package com.food.ordering.system.order.service.application.mediation.dto.exception;

import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Header;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class ExceptionMapper {

  @Handler
  public ErrorDTO handlerException(@Body OrderDomainException orderDomainException,
                                   @Header("codeAndReason") String codeAndReason) {
    return ErrorDTO.builder()
            .code(codeAndReason)
            .message(orderDomainException.getMessage())
            .build();
  }

}
