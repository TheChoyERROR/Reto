package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.ProductoRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final JpaProductoRepository jpaProductoRepository;

    @Autowired
    public ProductoRepositoryAdapter(JpaProductoRepository jpaProductoRepository) {
        this.jpaProductoRepository = jpaProductoRepository;
    }

    @Override
    public List<Producto> findAll() {
        return jpaProductoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return jpaProductoRepository.findById(id);
    }

    @Override
    public List<Producto> findByCategoriaId(Long categoriaId) {
        return jpaProductoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public Producto save(Producto producto) {
        return jpaProductoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        jpaProductoRepository.deleteById(id);
    }
}