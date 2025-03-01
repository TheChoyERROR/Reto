package com.example.demo.application;

import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.CarritoItem;
import com.example.demo.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface CarritoService {
    Optional<Carrito> getCarritoActivo(Long usuarioId);
    Carrito crearCarrito(Long usuarioId);
    CarritoItem agregarProducto(Long carritoId, Long productoId, Integer cantidad);
    CarritoItem actualizarCantidad(Long carritoId, Long itemId, Integer cantidad);
    void eliminarProducto(Long carritoId, Long itemId);
    void vaciarCarrito(Long carritoId);
    void procesarCarrito(Long carritoId);
    void abandonarCarrito(Long carritoId);
}