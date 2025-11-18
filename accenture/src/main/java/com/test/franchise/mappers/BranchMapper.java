package com.test.franchise.mappers;

import com.test.franchise.dto.response.BranchResponse;
import com.test.franchise.entities.Branch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface BranchMapper {
    BranchResponse toResponse(Branch branch);
}