package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestudentId restudentId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;



    // There fields are not final because we will set them in
    // bussiness logic during creation of order entity
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId=new TrackingId(UUID.randomUUID());
        orderStatus=orderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long itemId=1;
        for(OrderItem orderItem:items){
            orderItem.initializeOrderItem(super.getId(),new OrderItemId(itemId++));
        }
    }
    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay(){
        if(orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("Order status not in correct state for pay operation");
        }
        orderStatus=OrderStatus.PAID;
    }

    public void approve(){
        if(orderStatus !=OrderStatus.PAID){
            throw new OrderDomainException("Order not in correct state for approve operation ");
        }
        orderStatus=OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages){
        if(orderStatus !=OrderStatus.PAID){
            throw new OrderDomainException("Order should be paid state for cancelling");
        }
        orderStatus=OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);

    }
    public void updateFailureMessages(List<String> failureMessages){
        if(this.failureMessages !=null && failureMessages!=null){
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }

    }

    public void cancel(List<String> failureMessages){
        if(!(orderStatus==OrderStatus.CANCELLING || orderStatus==OrderStatus.PENDING)){
            throw new OrderDomainException("Order not in correct state for cancel operation ");

        }
        orderStatus=OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }


    private void validateItemsPrice() {
        Money orderItemTotal=items.stream().map(orderItem ->{
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
                }
        ).reduce(Money.ZERO,Money::add);

        if(!price.equals(orderItemTotal)){
            throw new OrderDomainException("Total price "+price.getamount()+"" +
                    " not equal to Order item price total "+orderItemTotal.getamount()+'!');
        }
    }

    private void validateOrderItem(OrderItem orderItem) {
        if(!orderItem.isValidPrice()){
            throw new OrderDomainException("Invalid");
        }


    }


    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isValidPrice()){
            throw new OrderDomainException("Order item price: "+orderItem.getPrice().getamount()+" " +
                    "is not valid for product "+orderItem.getProduct().getId()); // missing hai yaha
        }
    }

    private void validateTotalPrice() {
        if(price==null || !price.isGreaterThanZero())
            throw new OrderDomainException("Price must be greater than zero");
    }

    private void validateInitialOrder() {
        if(orderStatus!=null || getId()!=null){
            throw new OrderDomainException("Order not in correct state for initialization");

        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restudentId = builder.restudentId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestudentId getRestudentId() {
        return restudentId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestudentId restudentId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

                public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restudentId(RestudentId val) {
            restudentId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
