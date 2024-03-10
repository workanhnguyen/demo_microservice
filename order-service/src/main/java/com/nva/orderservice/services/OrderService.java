package com.nva.orderservice.services;

import com.nva.orderservice.dtos.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
