package com.example.demo.adapter.persistence;

import com.example.demo.domain.model.CarritoItem;
import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.CarritoItemRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CarritoItemRepositoryAdapter implements CarritoItemRepositoryPort {

    private final JpaCarritoItemRepository jpaCarritoItemRepository;

    @Autowired
    public CarritoItemRepositoryAdapter(JpaCarritoItemRepository jpaCarritoItemRepository) {
        this.jpaCarritoItemRepository = jpaCarritoItemRepository;
    }

    @Override
    public List<CarritoItem> findAll() {
        return jpaCarritoItemRepository.findAll();
    }

    @Override
    public Optional<CarritoItem> findById(Long id) {
        return jpaCarritoItemRepository.findById(id);
    }

    @Override
    public List<CarritoItem> findByCarrito(Carrito carrito) {
        return jpaCarritoItemRepository.findByCarrito(carrito);
    }

    @Override
    public Optional<CarritoItem> findByCarritoAndProducto(Carrito carrito, Producto producto) {
        return jpaCarritoItemRepository.findByCarritoAndProducto(carrito, producto);
    }

    @Override
    public CarritoItem save(CarritoItem carritoItem) {
        return jpaCarritoItemRepository.save(carritoItem);
    }

    @Override
    public void deleteById(Long id) {
        jpaCarritoItemRepository.deleteById(id);
    }
}