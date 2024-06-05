package com.food.ordering.system.restaurant.service;

import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
public class AppTest implements BaseTest {

  @Test
  @Disabled
  public void orderApprovedRepresentation() {
    var orderApproved = this.orderApprovaldMock();

    assertNotNull(orderApproved.getOrderId());
    assertEquals(orderApproved.getApprovalStatus(), OrderApprovalStatus.APPROVED);
  }


  @Test
  @Disabled
  public void productRepresentation() {

    var product = this.productMock();
    //
    assertNotNull(product.getId());
    assertEquals(product.getName(), "Product 1");
  }

  @Test
  @Disabled
  public void updateWithConfirmedPriceAndAvailable() {
    var product = this.productMock();
    product.updateWithConfirmedPriceAndAvailable(product.getName(), product.getPrice(), true);

    assertTrue(product.isAvailable());
  }

  @Test
  @Disabled
  public void orderDetailRepresentation() {
    var orderDetail = this.orderDetailMock();

    assertNotNull(orderDetail.getId());
    assertEquals(orderDetail.getOrderStatus(), OrderStatus.APPROVED);
  }

  @Test
  @Disabled
  public void restaurantRepresentation() {
    var restaurant = this.restaurantMock();
    assertNotNull(restaurant.getId());
    //restaurant.getOrderDetail().getId().getValue()
    assertTrue(restaurant.getOrderDetail().getProducts().size() > 1);
  }

  // test validateOrder
  @Test
  @Disabled
  public void validateOrderRestaurantRepresentation() {
    var restaurant = this.restaurantMock();
    var list = new ArrayList<String>();
    //
    restaurant.validateOrder(list);
  }

  // test constructOrderApproval
  @Test
  @Disabled
  public void constructOrderApprovalRepresentation() {
    var restaurant = this.restaurantMock();
    restaurant.constructOrderApproval(restaurant.getOrderApproval().getApprovalStatus());
    //
    assertNotNull(restaurant.getOrderApproval().getOrderId());
    assertEquals(restaurant.getOrderApproval().getApprovalStatus(), OrderApprovalStatus.APPROVED);
  }

  @Test
  public void orderApprovedEventRepresentation() {
    //
    var event = this.orderApprovedEventMock();
    assertNotNull(event.getRestaurantId());
    assertThat(event, instanceOf(OrderApprovalEvent.class));
    //assertThrows(ArgumentConversionException.class, ()-> not(instanceOf(OrderApprovalEvent.class)));
  }

  @Test
  public void orderRejectedEventRepresentation() {
    var event = this.orderRejectedEventMock();
    assertNotNull(event.getRestaurantId());
    assertThat(event, instanceOf(OrderApprovalEvent.class));
  }

}
