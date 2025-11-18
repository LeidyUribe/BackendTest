package com.test.franchise.services;

import com.test.franchise.dto.request.ProductRequest;
import com.test.franchise.dto.request.RenameRequest;
import com.test.franchise.dto.request.StockUpdateRequest;
import com.test.franchise.dto.response.MaxStockResponse;
import com.test.franchise.entities.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Long franchiseId, Long branchId, ProductRequest request);
    void deleteProduct(Long franchiseId, Long branchId, Long productId);
    Product updateStock(Long franchiseId, Long branchId, Long productId, StockUpdateRequest request);
    Product updateProductName(Long franchiseId, Long branchId, Long productId, RenameRequest request);
    List<MaxStockResponse> findMaxStockByBranch(Long franchiseId);
}