package com.test.franchise.services;

import com.test.franchise.dto.request.BranchRequest;
import com.test.franchise.dto.request.RenameRequest;
import com.test.franchise.entities.Branch;

public interface BranchService {
    Branch addBranch(Long franchiseId, BranchRequest request);
    Branch updateBranchName(Long franchiseId, Long branchId, RenameRequest request);
    Branch getBranch(Long franchiseId, Long branchId);
}