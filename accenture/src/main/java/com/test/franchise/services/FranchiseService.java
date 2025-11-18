package com.test.franchise.services;

import com.test.franchise.dto.request.FranchiseRequest;
import com.test.franchise.dto.request.RenameRequest;
import com.test.franchise.entities.Franchise;

import java.util.List;

public interface FranchiseService {
    Franchise createFranchise(FranchiseRequest request);
    Franchise updateFranchiseName(Long franchiseId, RenameRequest request);
    Franchise getFranchise(Long franchiseId);
    List<Franchise> findAll();
}