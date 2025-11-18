package com.test.franchise.dto.response;

public record ProductResponse(
        Long id,
        String name,
        Integer stock
) {}