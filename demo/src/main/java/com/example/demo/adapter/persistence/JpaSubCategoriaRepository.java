package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubCategoriaRepository extends JpaRepository<SubCategoria, Long> {
}