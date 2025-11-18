package com.test.franchise.dto.response;

import java.util.List;

public record BranchResponse(
        Long id,
        String name,
        List<ProductResponse> products
) {}