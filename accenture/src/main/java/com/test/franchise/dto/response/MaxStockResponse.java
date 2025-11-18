package com.test.franchise.dto.response;

public record MaxStockResponse(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {}