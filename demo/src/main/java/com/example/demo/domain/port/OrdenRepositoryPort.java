package com.example.demo.domain.port;

import com.example.demo.domain.model.Orden;
import com.example.demo.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface OrdenRepositoryPort {
    List<Orden> findAll();
    Optional<Orden> findById(Long id);
    Optional<Orden> findByNumeroOrden(String numeroOrden);
    List<Orden> findByUsuario(Usuario usuario);
    Orden save(Orden orden);
    void deleteById(Long id);
}