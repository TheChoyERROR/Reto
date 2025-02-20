package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final JpaCategoriaRepository jpaCategoriaRepository;

    @Autowired
    public CategoriaRepositoryAdapter(JpaCategoriaRepository jpaCategoriaRepository) {
        this.jpaCategoriaRepository = jpaCategoriaRepository;
    }

    @Override
    public List<Categoria> findAll() {
        return jpaCategoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return jpaCategoriaRepository.findById(id);
    }

    @Override
    public Categoria save(Categoria categoria) {
        return jpaCategoriaRepository.save(categoria);
    }

    @Override
    public void deleteById(Long id) {
        jpaCategoriaRepository.deleteById(id);
    }
}