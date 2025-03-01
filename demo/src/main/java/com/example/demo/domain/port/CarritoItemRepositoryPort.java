package com.example.demo.domain.port;

import com.example.demo.domain.model.CarritoItem;
import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface CarritoItemRepositoryPort {
    List<CarritoItem> findAll();
    Optional<CarritoItem> findById(Long id);
    List<CarritoItem> findByCarrito(Carrito carrito);
    Optional<CarritoItem> findByCarritoAndProducto(Carrito carrito, Producto producto);
    CarritoItem save(CarritoItem carritoItem);
    void deleteById(Long id);
}