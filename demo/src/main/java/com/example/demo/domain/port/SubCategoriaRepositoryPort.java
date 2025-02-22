package com.example.demo.domain.port;

import com.example.demo.domain.model.SubCategoria;
import java.util.List;
import java.util.Optional;

public interface SubCategoriaRepositoryPort {
    SubCategoria save(SubCategoria subCategoria);
    List<SubCategoria> findAll();
    Optional<SubCategoria> findById(Long id);
    void deleteById(Long id);
}