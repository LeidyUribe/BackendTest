package com.example.franchise.services;

import com.example.franchise.dto.request.FranchiseRequest;
import com.example.franchise.dto.request.RenameRequest;
import com.example.franchise.entities.Franchise;

import java.util.List;

public interface FranchiseService {
    Franchise createFranchise(FranchiseRequest request);
    Franchise updateFranchiseName(Long franchiseId, RenameRequest request);
    Franchise getFranchise(Long franchiseId);
    List<Franchise> findAll();
}