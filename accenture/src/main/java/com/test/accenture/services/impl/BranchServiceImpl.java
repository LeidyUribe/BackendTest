package com.example.franchise.services.impl;

import com.example.franchise.dto.request.BranchRequest;
import com.example.franchise.dto.request.RenameRequest;
import com.example.franchise.entities.Branch;
import com.example.franchise.entities.Franchise;
import com.example.franchise.exceptions.ResourceNotFoundException;
import com.example.franchise.repositories.BranchRepository;
import com.example.franchise.services.BranchService;
import com.example.franchise.services.FranchiseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final FranchiseService franchiseService;
    private final BranchRepository branchRepository;

    @Override
    public Branch addBranch(Long franchiseId, BranchRequest request) {
        Franchise franchise = franchiseService.getFranchise(franchiseId);
        Branch branch = new Branch();
        branch.setName(request.name());
        branch.setFranchise(franchise);
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranchName(Long franchiseId, Long branchId, RenameRequest request) {
        Branch branch = getBranch(franchiseId, branchId);
        branch.setName(request.name());
        return branchRepository.save(branch);
    }

    @Override
    public Branch getBranch(Long franchiseId, Long branchId) {
        return branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + branchId));
    }
}