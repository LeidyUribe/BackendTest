package com.test.franchise.controllers;

import com.test.franchise.dto.request.*;
import com.test.franchise.dto.response.BranchResponse;
import com.test.franchise.dto.response.FranchiseResponse;
import com.test.franchise.dto.response.MaxStockResponse;
import com.test.franchise.dto.response.ProductResponse;
import com.test.franchise.entities.Branch;
import com.test.franchise.entities.Franchise;
import com.test.franchise.entities.Product;
import com.test.franchise.mappers.BranchMapper;
import com.test.franchise.mappers.FranchiseMapper;
import com.test.franchise.mappers.ProductMapper;
import com.test.franchise.services.BranchService;
import com.test.franchise.services.FranchiseService;
import com.test.franchise.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franquicias")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;
    private final BranchService branchService;
    private final ProductService productService;
    private final FranchiseMapper franchiseMapper;
    private final BranchMapper branchMapper;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<FranchiseResponse> createFranchise(@Valid @RequestBody FranchiseRequest request) {
        Franchise franchise = franchiseService.createFranchise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(franchiseMapper.toResponse(franchise));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FranchiseResponse> renameFranchise(@PathVariable Long id,
                                                             @Valid @RequestBody RenameRequest request) {
        Franchise franchise = franchiseService.updateFranchiseName(id, request);
        return ResponseEntity.ok(franchiseMapper.toResponse(franchise));
    }

    @PostMapping("/{id}/sucursales")
    public ResponseEntity<BranchResponse> addBranch(@PathVariable Long id,
                                                    @Valid @RequestBody BranchRequest request) {
        Branch branch = branchService.addBranch(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(branchMapper.toResponse(branch));
    }

    @PatchMapping("/{id}/sucursales/{branchId}")
    public ResponseEntity<BranchResponse> renameBranch(@PathVariable Long id,
                                                       @PathVariable Long branchId,
                                                       @Valid @RequestBody RenameRequest request) {
        Branch branch = branchService.updateBranchName(id, branchId, request);
        return ResponseEntity.ok(branchMapper.toResponse(branch));
    }

    @PostMapping("/{id}/sucursales/{branchId}/productos")
    public ResponseEntity<ProductResponse> addProduct(@PathVariable Long id,
                                                      @PathVariable Long branchId,
                                                      @Valid @RequestBody ProductRequest request) {
        Product product = productService.addProduct(id, branchId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(product));
    }

    @PatchMapping("/{id}/sucursales/{branchId}/productos/{productId}")
    public ResponseEntity<ProductResponse> renameProduct(@PathVariable Long id,
                                                         @PathVariable Long branchId,
                                                         @PathVariable Long productId,
                                                         @Valid @RequestBody RenameRequest request) {
        Product product = productService.updateProductName(id, branchId, productId, request);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @DeleteMapping("/{id}/sucursales/{branchId}/productos/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id,
                                              @PathVariable Long branchId,
                                              @PathVariable Long productId) {
        productService.deleteProduct(id, branchId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/sucursales/{branchId}/productos/{productId}/stock")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id,
                                                       @PathVariable Long branchId,
                                                       @PathVariable Long productId,
                                                       @Valid @RequestBody StockUpdateRequest request) {
        Product product = productService.updateStock(id, branchId, productId, request);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @GetMapping("/{id}/productos-max-stock")
    public ResponseEntity<List<MaxStockResponse>> getMaxStockByBranch(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findMaxStockByBranch(id));
    }
}
