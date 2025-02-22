package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.SubCategoria;
import com.example.demo.domain.port.SubCategoriaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubCategoriaRepositoryAdapter implements SubCategoriaRepositoryPort {

    private final JpaSubCategoriaRepository jpaSubCategoriaRepository;

    public SubCategoriaRepositoryAdapter(JpaSubCategoriaRepository jpaSubCategoriaRepository) {
        this.jpaSubCategoriaRepository = jpaSubCategoriaRepository;
    }

    @Override
    public SubCategoria save(SubCategoria subCategoria) {
        return jpaSubCategoriaRepository.save(subCategoria);
    }

    @Override
    public List<SubCategoria> findAll() {
        return jpaSubCategoriaRepository.findAll();
    }

    @Override
    public Optional<SubCategoria> findById(Long id) {
        return jpaSubCategoriaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaSubCategoriaRepository.deleteById(id);
    }
}