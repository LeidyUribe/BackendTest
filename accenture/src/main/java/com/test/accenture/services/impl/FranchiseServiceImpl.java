package com.example.franchise.services.impl;

import com.example.franchise.dto.request.FranchiseRequest;
import com.example.franchise.dto.request.RenameRequest;
import com.example.franchise.entities.Franchise;
import com.example.franchise.exceptions.BusinessException;
import com.example.franchise.exceptions.ResourceNotFoundException;
import com.example.franchise.repositories.FranchiseRepository;
import com.example.franchise.services.FranchiseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Franchise createFranchise(FranchiseRequest request) {
        franchiseRepository.findByNameIgnoreCase(request.name())
                .ifPresent(existing -> {
                    throw new BusinessException("La franquicia ya existe");
                });
        Franchise franchise = new Franchise();
        franchise.setName(request.name());
        return franchiseRepository.save(franchise);
    }

    @Override
    public Franchise updateFranchiseName(Long franchiseId, RenameRequest request) {
        Franchise franchise = getFranchise(franchiseId);
        franchise.setName(request.name());
        return franchiseRepository.save(franchise);
    }

    @Override
    @Transactional
    public Franchise getFranchise(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia no encontrada: " + franchiseId));
    }

    @Override
    @Transactional
    public List<Franchise> findAll() {
        return franchiseRepository.findAll();
    }
}
