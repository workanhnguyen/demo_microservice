package com.nva.inventoryservice.services;

import com.nva.inventoryservice.dtos.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> skuCode);
}
