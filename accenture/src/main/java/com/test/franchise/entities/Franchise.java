package com.test.franchise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "franchises")
@Getter
@Setter
@ToString(exclude = "branches")
public class Franchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @OneToMany(mappedBy = "franchise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branches = new ArrayList<>();

    public void addBranch(Branch branch) {
        branches.add(branch);
        branch.setFranchise(this);
    }
}