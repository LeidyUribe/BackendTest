package com.test.franchise.repositories;

import com.test.franchise.entities.Branch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByIdAndFranchiseId(Long branchId, Long franchiseId);

    @EntityGraph(value = "Branch.products")
    List<Branch> findByFranchiseId(Long franchiseId);
}