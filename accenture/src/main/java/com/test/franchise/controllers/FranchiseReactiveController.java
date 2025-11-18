package com.test.franchise.controllers;

import com.test.franchise.dto.response.FranchiseResponse;
import com.test.franchise.mappers.FranchiseMapper;
import com.test.franchise.services.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactivo/franquicias")
@RequiredArgsConstructor
public class FranchiseReactiveController {

    private final FranchiseService franchiseService;
    private final FranchiseMapper franchiseMapper;

    @GetMapping
    public Flux<FranchiseResponse> getAllFranchises() {
        return Flux.defer(() -> Flux.fromIterable(franchiseService.findAll())
                .map(franchiseMapper::toResponse));
    }

    @GetMapping("/{id}")
    public Mono<FranchiseResponse> getFranchise(Long id) {
        return Mono.fromCallable(() -> franchiseMapper.toResponse(franchiseService.getFranchise(id)));
    }
}