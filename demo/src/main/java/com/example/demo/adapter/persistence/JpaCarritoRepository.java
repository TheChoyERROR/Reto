package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaCarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, Carrito.Estado estado);
    
    // Métodos adicionales para estadísticas o reporting si se necesitaran
    long countByEstado(Carrito.Estado estado);
}