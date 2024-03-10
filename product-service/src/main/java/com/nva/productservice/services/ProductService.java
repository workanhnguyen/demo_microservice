package com.nva.productservice.services;


import com.nva.productservice.dtos.ProductRequest;
import com.nva.productservice.dtos.ProductResponse;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
}
