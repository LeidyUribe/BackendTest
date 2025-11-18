package com.example.franchise.services.impl;

import com.example.franchise.dto.request.ProductRequest;
import com.example.franchise.dto.request.RenameRequest;
import com.example.franchise.dto.request.StockUpdateRequest;
import com.example.franchise.dto.response.MaxStockResponse;
import com.example.franchise.entities.Branch;
import com.example.franchise.entities.Product;
import com.example.franchise.exceptions.ResourceNotFoundException;
import com.example.franchise.repositories.BranchRepository;
import com.example.franchise.repositories.ProductRepository;
import com.example.franchise.services.BranchService;
import com.example.franchise.services.ProductService;
import com.example.franchise.utils.StockUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final BranchService branchService;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public Product addProduct(Long franchiseId, Long branchId, ProductRequest request) {
        Branch branch = branchService.getBranch(franchiseId, branchId);
        Product product = new Product();
        product.setName(request.name());
        product.setStock(request.stock());
        product.setBranch(branch);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long franchiseId, Long branchId, Long productId) {
        Product product = getProduct(franchiseId, branchId, productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateStock(Long franchiseId, Long branchId, Long productId, StockUpdateRequest request) {
        Product product = getProduct(franchiseId, branchId, productId);
        product.setStock(request.stock());
        return productRepository.save(product);
    }

    @Override
    public Product updateProductName(Long franchiseId, Long branchId, Long productId, RenameRequest request) {
        Product product = getProduct(franchiseId, branchId, productId);
        product.setName(request.name());
        return productRepository.save(product);
    }

    @Override
    public List<MaxStockResponse> findMaxStockByBranch(Long franchiseId) {
        List<Branch> branches = branchRepository.findByFranchiseId(franchiseId);
        if (branches.isEmpty()) {
            throw new ResourceNotFoundException("Franquicia sin sucursales o inexistente: " + franchiseId);
        }

        return branches.stream()
                .map(branch -> StockUtils.highestStock(branch.getProducts())
                        .map(product -> new MaxStockResponse(
                                branch.getId(),
                                branch.getName(),
                                product.getId(),
                                product.getName(),
                                product.getStock()
                        ))
                        .orElse(new MaxStockResponse(
                                branch.getId(),
                                branch.getName(),
                                null,
                                null,
                                0
                        )))
                .toList();
    }

    private Product getProduct(Long franchiseId, Long branchId, Long productId) {
        branchService.getBranch(franchiseId, branchId);
        return productRepository.findByIdAndBranchId(productId, branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productId));
    }
}