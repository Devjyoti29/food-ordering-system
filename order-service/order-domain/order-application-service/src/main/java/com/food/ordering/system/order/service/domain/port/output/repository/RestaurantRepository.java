package com.food.ordering.system.order.service.domain.port.output.repository;

import com.food.ordering.system.order.service.domain.entity.Restudent;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restudent> findRestaurantInformation(Restudent restudent);
}
