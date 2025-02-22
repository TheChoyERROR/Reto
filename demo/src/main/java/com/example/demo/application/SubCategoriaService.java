package com.example.demo.application;

import com.example.demo.domain.model.SubCategoria;
import java.util.List;
import java.util.Optional;

public interface SubCategoriaService {
    SubCategoria createSubCategoria(Long categoriaId, String nombre);
    List<SubCategoria> listSubCategorias();
    Optional<SubCategoria> updateSubCategoria(Long id, Long categoriaId, String nombre);
    void deleteSubCategoria(Long id);
}