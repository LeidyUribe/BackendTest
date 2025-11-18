package com.test.franchise.repositories;

import com.test.franchise.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndBranchId(Long productId, Long branchId);
}