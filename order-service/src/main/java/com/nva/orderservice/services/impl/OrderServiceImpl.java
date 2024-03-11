package com.nva.orderservice.services.impl;

import com.nva.orderservice.dtos.InventoryResponse;
import com.nva.orderservice.dtos.OrderLineItemDto;
import com.nva.orderservice.dtos.OrderRequest;
import com.nva.orderservice.models.Order;
import com.nva.orderservice.models.OrderLineItem;
import com.nva.orderservice.repositories.OrderRepository;
import com.nva.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemDtoList()
                .stream()
                .map(this::mapToDto).toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItemList)
                .build();

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        // Call Inventory Service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventories",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve().bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
            return "Order placed successfully!";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .price(orderLineItemDto.getPrice())
                .skuCode(orderLineItemDto.getSkuCode())
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }
}
