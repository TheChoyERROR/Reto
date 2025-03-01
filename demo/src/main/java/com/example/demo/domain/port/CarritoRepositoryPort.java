package com.example.demo.domain.port;

import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface CarritoRepositoryPort {
    List<Carrito> findAll();
    Optional<Carrito> findById(Long id);
    Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, Carrito.Estado estado);
    Carrito save(Carrito carrito);
    void deleteById(Long id);
}