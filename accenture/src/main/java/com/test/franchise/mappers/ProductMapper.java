package com.test.franchise.mappers;

import com.test.franchise.dto.response.ProductResponse;
import com.test.franchise.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
}