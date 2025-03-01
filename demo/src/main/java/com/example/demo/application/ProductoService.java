package com.example.demo.application;

import com.example.demo.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> getAllProductos();
    Optional<Producto> getProductoById(Long id);
    List<Producto> getProductosByCategoria(Long categoriaId);
    Producto createProducto(Producto producto);
    Producto updateProducto(Long id, Producto producto);
    void deleteProducto(Long id);
    boolean actualizarStock(Long id, Integer cantidad);
}