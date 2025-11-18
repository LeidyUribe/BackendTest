package com.test.franchise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchRequest(
        @NotBlank @Size(max = 120) String name
) {}