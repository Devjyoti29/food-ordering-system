package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.ResturantApprovalResponse;
import com.food.ordering.system.order.service.domain.port.input.message.listener.restaurantapproval.ResturantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements ResturantApprovalMessageListener {


    @Override
    public void orderApproved(ResturantApprovalResponse resturantApprovalResponse) {

    }

    @Override
    public void orderRejected(ResturantApprovalResponse resturantApprovalResponse) {

    }
}
