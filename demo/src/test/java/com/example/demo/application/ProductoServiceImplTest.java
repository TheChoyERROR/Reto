package com.example.demo.application;

import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoServiceImplTest {

    private ProductoRepositoryPort productoRepository;
    private CategoriaRepositoryPort categoriaRepository;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoRepository = Mockito.mock(ProductoRepositoryPort.class);
        categoriaRepository = Mockito.mock(CategoriaRepositoryPort.class);
        productoService = new ProductoServiceImpl(productoRepository, categoriaRepository);
    }

    @Test
    void testGetAllProductos() {
        // Preparar
        Producto prod1 = new Producto("Producto 1", "Descripción 1", new BigDecimal("100"), 10);
        prod1.setId(1L);
        Producto prod2 = new Producto("Producto 2", "Descripción 2", new BigDecimal("200"), 20);
        prod2.setId(2L);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(prod1, prod2));

        // Ejecutar
        List<Producto> result = productoService.getAllProductos();

        // Verificar
        assertEquals(2, result.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGetProductosByCategoria() {
        // Preparar
        Long categoriaId = 1L;
        Producto prod1 = new Producto("Producto 1", "Descripción 1", new BigDecimal("100"), 10);
        when(productoRepository.findByCategoriaId(categoriaId)).thenReturn(Arrays.asList(prod1));

        // Ejecutar
        List<Producto> result = productoService.getProductosByCategoria(categoriaId);

        // Verificar
        assertEquals(1, result.size());
        assertEquals("Producto 1", result.get(0).getNombre());
        verify(productoRepository, times(1)).findByCategoriaId(categoriaId);
    }

    @Test
    void testCreateProducto() {
        // Preparar
        Categoria categoria = new Categoria("Categoría 1");
        categoria.setId(1L);
        
        Producto producto = new Producto("Nuevo Producto", "Descripción", new BigDecimal("150"), 5);
        producto.setCategoria(categoria);
        
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Ejecutar
        Producto result = productoService.createProducto(producto);

        // Verificar
        assertNotNull(result);
        assertEquals("Nuevo Producto", result.getNombre());
        assertEquals(categoria, result.getCategoria());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testActualizarStockSuficiente() {
        // Preparar
        Long productoId = 1L;
        Producto producto = new Producto("Producto", "Descripción", new BigDecimal("100"), 10);
        producto.setId(productoId);
        
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Ejecutar
        boolean result = productoService.actualizarStock(productoId, 3);

        // Verificar
        assertTrue(result);
        assertEquals(7, producto.getStock()); // 10 - 3 = 7
        verify(productoRepository).save(producto);
    }

    @Test
    void testActualizarStockInsuficiente() {
        // Preparar
        Long productoId = 1L;
        Producto producto = new Producto("Producto", "Descripción", new BigDecimal("100"), 5);
        producto.setId(productoId);
        
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));

        // Ejecutar
        boolean result = productoService.actualizarStock(productoId, 10);

        // Verificar
        assertFalse(result);
        assertEquals(5, producto.getStock()); // El stock no debe cambiar
        verify(productoRepository, never()).save(any(Producto.class));
    }
}