package com.test.franchise.services.impl;

import com.test.franchise.dto.request.BranchRequest;
import com.test.franchise.dto.request.RenameRequest;
import com.test.franchise.entities.Branch;
import com.test.franchise.entities.Franchise;
import com.test.franchise.exceptions.ResourceNotFoundException;
import com.test.franchise.repositories.BranchRepository;
import com.test.franchise.services.BranchService;
import com.test.franchise.services.FranchiseService;
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