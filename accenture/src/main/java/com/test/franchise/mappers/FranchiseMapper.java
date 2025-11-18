package com.test.franchise.mappers;

import com.test.franchise.dto.request.FranchiseRequest;
import com.test.franchise.dto.response.FranchiseResponse;
import com.test.franchise.entities.Franchise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BranchMapper.class})
public interface FranchiseMapper {
    Franchise toEntity(FranchiseRequest request);

    @Mapping(target = "branches", source = "branches")
    FranchiseResponse toResponse(Franchise franchise);
}
