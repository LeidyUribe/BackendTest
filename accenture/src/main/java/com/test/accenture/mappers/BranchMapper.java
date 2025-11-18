package com.example.franchise.mappers;

import com.example.franchise.dto.response.BranchResponse;
import com.example.franchise.entities.Branch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface BranchMapper {
    BranchResponse toResponse(Branch branch);
}