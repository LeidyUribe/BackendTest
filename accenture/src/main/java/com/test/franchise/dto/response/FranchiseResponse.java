package com.test.franchise.dto.response;

import java.util.List;

public record FranchiseResponse(
        Long id,
        String name,
        List<BranchResponse> branches
) {}