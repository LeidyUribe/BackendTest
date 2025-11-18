package com.example.franchise.services;

import com.example.franchise.dto.request.ProductRequest;
import com.example.franchise.dto.request.RenameRequest;
import com.example.franchise.dto.request.StockUpdateRequest;
import com.example.franchise.dto.response.MaxStockResponse;
import com.example.franchise.entities.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Long franchiseId, Long branchId, ProductRequest request);
    void deleteProduct(Long franchiseId, Long branchId, Long productId);
    Product updateStock(Long franchiseId, Long branchId, Long productId, StockUpdateRequest request);
    Product updateProductName(Long franchiseId, Long branchId, Long productId, RenameRequest request);
    List<MaxStockResponse> findMaxStockByBranch(Long franchiseId);
}