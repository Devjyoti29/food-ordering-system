package com.food.ordering.system.order.service.domain.port.output.message.publisher.restudentapproval;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidResturantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {


}
