package com.example.demo.application;

import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepositoryPort categoriaRepositoryPort;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepositoryPort categoriaRepositoryPort) {
        this.categoriaRepositoryPort = categoriaRepositoryPort;
    }

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepositoryPort.findAll();
    }

    @Override
    public java.util.Optional<Categoria> getCategoriaById(Long id) {
        return categoriaRepositoryPort.findById(id);
    }

    @Override
    public Categoria createCategoria(Categoria categoria) {
        return categoriaRepositoryPort.save(categoria);
    }

    @Override
    public Categoria updateCategoria(Long id, Categoria categoria) {
        return categoriaRepositoryPort.findById(id)
            .map(existing -> {
                categoria.setId(id);
                return categoriaRepositoryPort.save(categoria);
            }).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }

    @Override
    public void deleteCategoria(Long id) {
        categoriaRepositoryPort.deleteById(id);
    }
}