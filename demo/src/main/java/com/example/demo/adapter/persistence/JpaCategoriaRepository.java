package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoriaRepository extends JpaRepository<Categoria, Long> {
}