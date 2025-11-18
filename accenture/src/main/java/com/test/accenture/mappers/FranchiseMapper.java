package com.example.franchise.mappers;

import com.example.franchise.dto.request.FranchiseRequest;
import com.example.franchise.dto.response.FranchiseResponse;
import com.example.franchise.entities.Franchise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BranchMapper.class})
public interface FranchiseMapper {
    Franchise toEntity(FranchiseRequest request);

    @Mapping(target = "branches", source = "branches")
    FranchiseResponse toResponse(Franchise franchise);
}
