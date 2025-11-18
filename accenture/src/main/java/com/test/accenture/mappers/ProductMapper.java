package com.example.franchise.mappers;

import com.example.franchise.dto.response.ProductResponse;
import com.example.franchise.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
}