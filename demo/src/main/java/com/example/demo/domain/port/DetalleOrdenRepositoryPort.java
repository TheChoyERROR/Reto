package com.example.demo.domain.port;

import com.example.demo.domain.model.DetalleOrden;
import com.example.demo.domain.model.Orden;
import java.util.List;
import java.util.Optional;

public interface DetalleOrdenRepositoryPort {
    List<DetalleOrden> findAll();
    Optional<DetalleOrden> findById(Long id);
    List<DetalleOrden> findByOrden(Orden orden);
    DetalleOrden save(DetalleOrden detalleOrden);
    void deleteById(Long id);
}