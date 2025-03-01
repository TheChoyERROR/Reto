package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.DetalleOrden;
import com.example.demo.domain.model.Orden;
import com.example.demo.domain.port.DetalleOrdenRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DetalleOrdenRepositoryAdapter implements DetalleOrdenRepositoryPort {

    private final JpaDetalleOrdenRepository jpaDetalleOrdenRepository;

    @Autowired
    public DetalleOrdenRepositoryAdapter(JpaDetalleOrdenRepository jpaDetalleOrdenRepository) {
        this.jpaDetalleOrdenRepository = jpaDetalleOrdenRepository;
    }

    @Override
    public List<DetalleOrden> findAll() {
        return jpaDetalleOrdenRepository.findAll();
    }

    @Override
    public Optional<DetalleOrden> findById(Long id) {
        return jpaDetalleOrdenRepository.findById(id);
    }

    @Override
    public List<DetalleOrden> findByOrden(Orden orden) {
        return jpaDetalleOrdenRepository.findByOrden(orden);
    }

    @Override
    public DetalleOrden save(DetalleOrden detalleOrden) {
        return jpaDetalleOrdenRepository.save(detalleOrden);
    }

    @Override
    public void deleteById(Long id) {
        jpaDetalleOrdenRepository.deleteById(id);
    }
}