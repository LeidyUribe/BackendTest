package com.test.franchise.controllers;

import com.test.franchise.dto.request.FranchiseRequest;
import com.test.franchise.repositories.FranchiseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FranchiseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFranchise_returns201() throws Exception {
        FranchiseRequest request = new FranchiseRequest("Integration Franquicia");

        mockMvc.perform(post("/franquicias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Integration Franquicia")));
    }
}