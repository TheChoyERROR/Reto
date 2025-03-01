package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Orden;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.OrdenRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdenRepositoryAdapter implements OrdenRepositoryPort {

    private final JpaOrdenRepository jpaOrdenRepository;

    @Autowired
    public OrdenRepositoryAdapter(JpaOrdenRepository jpaOrdenRepository) {
        this.jpaOrdenRepository = jpaOrdenRepository;
    }

    @Override
    public List<Orden> findAll() {
        return jpaOrdenRepository.findAll();
    }

    @Override
    public Optional<Orden> findById(Long id) {
        return jpaOrdenRepository.findById(id);
    }

    @Override
    public Optional<Orden> findByNumeroOrden(String numeroOrden) {
        return jpaOrdenRepository.findByNumeroOrden(numeroOrden);
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return jpaOrdenRepository.findByUsuario(usuario);
    }

    @Override
    public Orden save(Orden orden) {
        return jpaOrdenRepository.save(orden);
    }

    @Override
    public void deleteById(Long id) {
        jpaOrdenRepository.deleteById(id);
    }
}