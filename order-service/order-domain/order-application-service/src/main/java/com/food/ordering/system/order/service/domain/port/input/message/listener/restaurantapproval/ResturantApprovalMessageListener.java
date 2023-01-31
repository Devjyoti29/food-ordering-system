package com.food.ordering.system.order.service.domain.port.input.message.listener.restaurantapproval;

import com.food.ordering.system.order.service.domain.dto.message.ResturantApprovalResponse;

public interface ResturantApprovalMessageListener {
    void orderApproved(ResturantApprovalResponse resturantApprovalResponse);
    void orderRejected(ResturantApprovalResponse resturantApprovalResponse);
}
