package com.example.franchise.repositories;

import com.example.franchise.entities.Franchise;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

    Optional<Franchise> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = "branches")
    Optional<Franchise> findWithBranchesById(Long id);
}