package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.CarritoRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CarritoRepositoryAdapter implements CarritoRepositoryPort {

    private final JpaCarritoRepository jpaCarritoRepository;

    @Autowired
    public CarritoRepositoryAdapter(JpaCarritoRepository jpaCarritoRepository) {
        this.jpaCarritoRepository = jpaCarritoRepository;
    }

    @Override
    public List<Carrito> findAll() {
        return jpaCarritoRepository.findAll();
    }

    @Override
    public Optional<Carrito> findById(Long id) {
        return jpaCarritoRepository.findById(id);
    }

    @Override
    public Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, Carrito.Estado estado) {
        return jpaCarritoRepository.findByUsuarioAndEstado(usuario, estado);
    }

    @Override
    public Carrito save(Carrito carrito) {
        return jpaCarritoRepository.save(carrito);
    }

    @Override
    public void deleteById(Long id) {
        jpaCarritoRepository.deleteById(id);
    }
}