package com.test.franchise.services.impl;

import com.test.franchise.dto.request.FranchiseRequest;
import com.test.franchise.dto.request.RenameRequest;
import com.test.franchise.entities.Franchise;
import com.test.franchise.exceptions.BusinessException;
import com.test.franchise.exceptions.ResourceNotFoundException;
import com.test.franchise.repositories.FranchiseRepository;
import com.test.franchise.services.FranchiseService;
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
