package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.DetalleOrden;
import com.example.demo.domain.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    List<DetalleOrden> findByOrden(Orden orden);
}