package com.example.demo.domain.port;

import com.example.demo.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    List<Producto> findByCategoriaId(Long categoriaId);
    Producto save(Producto producto);
    void deleteById(Long id);
}