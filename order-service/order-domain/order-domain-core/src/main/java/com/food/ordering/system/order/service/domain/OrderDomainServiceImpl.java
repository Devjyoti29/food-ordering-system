package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restudent;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{

    private static final String UTC="UTC";

    @Override
    public OrderCreatedEvent validateAndInitializeOrder(Order order, Restudent restudent) {
        validateRestudent(restudent);
        setOrderProductImformation(order,restudent);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id:{} is initialized ",order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void setOrderProductImformation(Order order, Restudent restudent) {
        order.getItems().forEach(orderItem -> restudent.
                getProducts().forEach(restudentProduct->{
                    Product currentProduct= orderItem.getProduct();
                    if(currentProduct.equals(restudentProduct)){
                        currentProduct.updateWithConfirmedNameAndPrice(
                                restudentProduct.getName(),restudentProduct.getPrice()
                        );
                    }
                }));
    }

    private void validateRestudent(Restudent restudent){
        if(!restudent.isActive()){
            throw new OrderDomainException("Restaurant with id "+restudent.getId().getValue() +
                    " is currently not active");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {}",order.getId().getValue());
        return null;
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id :{} is approved",order.getId().getValue());

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id :{}",order.getId().getValue());
        return new OrderCancelledEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id : {} is cancelled",order.getId().getValue());
    }
}
