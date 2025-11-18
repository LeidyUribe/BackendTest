package com.example.franchise.dto.response;

public record ProductResponse(
        Long id,
        String name,
        Integer stock
) {}