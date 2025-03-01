package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.Orden;
import com.example.demo.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaOrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByNumeroOrden(String numeroOrden);
    List<Orden> findByUsuario(Usuario usuario);
    List<Orden> findByEstado(Orden.Estado estado);
    List<Orden> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);
}