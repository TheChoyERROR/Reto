package com.example.demo.application;

import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final CategoriaRepositoryPort categoriaRepositoryPort;

    @Autowired
    public ProductoServiceImpl(ProductoRepositoryPort productoRepositoryPort, 
                               CategoriaRepositoryPort categoriaRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.categoriaRepositoryPort = categoriaRepositoryPort;
    }

    @Override
    public List<Producto> getAllProductos() {
        return productoRepositoryPort.findAll();
    }

    @Override
    public Optional<Producto> getProductoById(Long id) {
        return productoRepositoryPort.findById(id);
    }

    @Override
    public List<Producto> getProductosByCategoria(Long categoriaId) {
        return productoRepositoryPort.findByCategoriaId(categoriaId);
    }

    @Override
    @Transactional
    public Producto createProducto(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            categoriaRepositoryPort.findById(producto.getCategoria().getId())
                .ifPresent(producto::setCategoria);
        }
        return productoRepositoryPort.save(producto);
    }

    @Override
    @Transactional
    public Producto updateProducto(Long id, Producto producto) {
        return productoRepositoryPort.findById(id)
            .map(existing -> {
                producto.setId(id);
                // Asegurar que la categoría existe si se proporciona
                if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
                    categoriaRepositoryPort.findById(producto.getCategoria().getId())
                        .ifPresent(producto::setCategoria);
                }
                return productoRepositoryPort.save(producto);
            }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    @Transactional
    public void deleteProducto(Long id) {
        productoRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional
    public boolean actualizarStock(Long id, Integer cantidad) {
        Optional<Producto> optProducto = productoRepositoryPort.findById(id);
        if (optProducto.isPresent()) {
            Producto producto = optProducto.get();
            int nuevoStock = producto.getStock() - cantidad;
            
            if (nuevoStock < 0) {
                return false; // No hay suficiente stock
            }
            
            producto.setStock(nuevoStock);
            productoRepositoryPort.save(producto);
            return true;
        }
        return false;
    }
}