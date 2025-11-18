package com.example.franchise.services;

import com.example.franchise.dto.request.BranchRequest;
import com.example.franchise.dto.request.RenameRequest;
import com.example.franchise.entities.Branch;

public interface BranchService {
    Branch addBranch(Long franchiseId, BranchRequest request);
    Branch updateBranchName(Long franchiseId, Long branchId, RenameRequest request);
    Branch getBranch(Long franchiseId, Long branchId);
}