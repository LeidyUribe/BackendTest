package com.test.franchise.services;

import com.test.franchise.dto.request.FranchiseRequest;
import com.test.franchise.dto.request.RenameRequest;
import com.test.franchise.entities.Franchise;
import com.test.franchise.exceptions.BusinessException;
import com.test.franchise.repositories.FranchiseRepository;
import com.test.franchise.services.impl.FranchiseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FranchiseServiceTest {

    private FranchiseRepository franchiseRepository;
    private FranchiseService franchiseService;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        franchiseService = new FranchiseServiceImpl(franchiseRepository);
    }

    @Test
    void createFranchise_ok() {
        FranchiseRequest request = new FranchiseRequest("Nueva Franquicia");
        when(franchiseRepository.findByNameIgnoreCase("Nueva Franquicia")).thenReturn(Optional.empty());
        Franchise persisted = new Franchise();
        persisted.setId(1L);
        persisted.setName("Nueva Franquicia");
        when(franchiseRepository.save(any())).thenReturn(persisted);

        Franchise result = franchiseService.createFranchise(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(franchiseRepository).save(any());
    }

    @Test
    void createFranchise_duplicateName_throwsBusiness() {
        FranchiseRequest request = new FranchiseRequest("Existente");
        when(franchiseRepository.findByNameIgnoreCase("Existente")).thenReturn(Optional.of(new Franchise()));

        assertThatThrownBy(() -> franchiseService.createFranchise(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void renameFranchise_ok() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Old Name");
        when(franchiseRepository.findById(1L)).thenReturn(Optional.of(franchise));
        when(franchiseRepository.save(franchise)).thenReturn(franchise);

        Franchise updated = franchiseService.updateFranchiseName(1L, new RenameRequest("New Name"));

        assertThat(updated.getName()).isEqualTo("New Name");
    }
}