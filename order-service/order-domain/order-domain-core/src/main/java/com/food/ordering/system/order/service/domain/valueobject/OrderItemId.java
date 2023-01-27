package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> { // In our Aggregate uniqueness of orderId is only
                                                // required. Hence we are using Long not UUID
    public OrderItemId(Long value) {
        super(value);
    }

}
