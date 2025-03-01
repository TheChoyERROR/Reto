package com.example.demo.application;

import com.example.demo.domain.model.*;
import com.example.demo.domain.port.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarritoServiceImplTest {

    private CarritoRepositoryPort carritoRepository;
    private CarritoItemRepositoryPort carritoItemRepository;
    private ProductoRepositoryPort productoRepository;
    private UsuarioRepositoryPort usuarioRepository;
    private CarritoService carritoService;

    @BeforeEach
    void setUp() {
        carritoRepository = Mockito.mock(CarritoRepositoryPort.class);
        carritoItemRepository = Mockito.mock(CarritoItemRepositoryPort.class);
        productoRepository = Mockito.mock(ProductoRepositoryPort.class);
        usuarioRepository = Mockito.mock(UsuarioRepositoryPort.class);
        carritoService = new CarritoServiceImpl(
            carritoRepository, 
            carritoItemRepository, 
            productoRepository, 
            usuarioRepository
        );
    }

    @Test
    void testCrearCarrito() {
        // Preparar
        Long usuarioId = 1L;
        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        usuario.setId(usuarioId);
        
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(carritoRepository.findByUsuarioAndEstado(usuario, Carrito.Estado.ACTIVO))
            .thenReturn(Optional.empty());
            
        Carrito nuevoCarrito = new Carrito(usuario);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(nuevoCarrito);

        // Ejecutar
        Carrito result = carritoService.crearCarrito(usuarioId);

        // Verificar
        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals(Carrito.Estado.ACTIVO, result.getEstado());
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void testAgregarProducto() {
        // Preparar
        Long carritoId = 1L;
        Long productoId = 2L;
        Integer cantidad = 3;

        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        usuario.setId(1L);
        
        Carrito carrito = new Carrito(usuario);
        carrito.setId(carritoId);
        carrito.setEstado(Carrito.Estado.ACTIVO);
        
        Producto producto = new Producto("Producto", "Descripción", new BigDecimal("50"), 10);
        producto.setId(productoId);
        producto.setActivo(true);
        
        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(carritoItemRepository.findByCarritoAndProducto(carrito, producto))
            .thenReturn(Optional.empty());
            
        CarritoItem nuevoItem = new CarritoItem(carrito, producto, cantidad);
        when(carritoItemRepository.save(any(CarritoItem.class))).thenReturn(nuevoItem);

        // Ejecutar
        CarritoItem result = carritoService.agregarProducto(carritoId, productoId, cantidad);

        // Verificar
        assertNotNull(result);
        assertEquals(carrito, result.getCarrito());
        assertEquals(producto, result.getProducto());
        assertEquals(cantidad, result.getCantidad());
        verify(carritoItemRepository).save(any(CarritoItem.class));
    }

    @Test
    void testAgregarProductoYaExistenteIncrementaCantidad() {
        // Preparar
        Long carritoId = 1L;
        Long productoId = 2L;
        Integer cantidadNueva = 2;

        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        Carrito carrito = new Carrito(usuario);
        carrito.setId(carritoId);
        carrito.setEstado(Carrito.Estado.ACTIVO);
        
        Producto producto = new Producto("Producto", "Descripción", new BigDecimal("50"), 10);
        producto.setId(productoId);
        producto.setActivo(true);
        
        CarritoItem itemExistente = new CarritoItem(carrito, producto, 3); // Ya tenía 3 unidades
        
        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(carritoItemRepository.findByCarritoAndProducto(carrito, producto))
            .thenReturn(Optional.of(itemExistente));
            
        when(carritoItemRepository.save(any(CarritoItem.class))).thenAnswer(i -> i.getArgument(0));

        // Ejecutar
        CarritoItem result = carritoService.agregarProducto(carritoId, productoId, cantidadNueva);

        // Verificar
        assertEquals(5, result.getCantidad()); // 3 + 2 = 5
        verify(carritoItemRepository).save(itemExistente);
    }

    @Test
    void testProcesarCarrito() {
        // Preparar
        Long carritoId = 1L;
        Carrito carrito = new Carrito();
        carrito.setId(carritoId);
        carrito.setEstado(Carrito.Estado.ACTIVO);
        
        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        
        ArgumentCaptor<Carrito> carritoCaptor = ArgumentCaptor.forClass(Carrito.class);
        when(carritoRepository.save(carritoCaptor.capture())).thenReturn(carrito);

        // Ejecutar
        carritoService.procesarCarrito(carritoId);

        // Verificar
        assertEquals(Carrito.Estado.PROCESADO, carritoCaptor.getValue().getEstado());
    }
}