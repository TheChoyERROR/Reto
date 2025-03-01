package com.example.demo.application;

import com.example.demo.domain.model.*;
import com.example.demo.domain.port.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrdenServiceImplTest {

    private OrdenRepositoryPort ordenRepository;
    private DetalleOrdenRepositoryPort detalleOrdenRepository;
    private CarritoRepositoryPort carritoRepository;
    private UsuarioRepositoryPort usuarioRepository;
    private ProductoServiceImpl productoService;
    private OrdenService ordenService;

    @BeforeEach
    void setUp() {
        ordenRepository = Mockito.mock(OrdenRepositoryPort.class);
        detalleOrdenRepository = Mockito.mock(DetalleOrdenRepositoryPort.class);
        carritoRepository = Mockito.mock(CarritoRepositoryPort.class);
        usuarioRepository = Mockito.mock(UsuarioRepositoryPort.class);
        productoService = Mockito.mock(ProductoServiceImpl.class);
        ordenService = new OrdenServiceImpl(
            ordenRepository, 
            detalleOrdenRepository, 
            carritoRepository, 
            usuarioRepository, 
            productoService
        );
    }

    @Test
    void testGetOrdenesByUsuario() {
        // Preparar
        Long usuarioId = 1L;
        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        usuario.setId(usuarioId);
        
        Orden orden1 = new Orden(usuario, "Dirección 1", "Tarjeta");
        orden1.setId(1L);
        Orden orden2 = new Orden(usuario, "Dirección 2", "PayPal");
        orden2.setId(2L);
        
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(ordenRepository.findByUsuario(usuario)).thenReturn(Arrays.asList(orden1, orden2));

        // Ejecutar
        List<Orden> result = ordenService.getOrdenesByUsuario(usuarioId);

        // Verificar
        assertEquals(2, result.size());
        assertEquals(orden1.getId(), result.get(0).getId());
        assertEquals(orden2.getId(), result.get(1).getId());
    }

    @Test
    void testCrearOrdenDesdeCarrito() {
        // Preparar
        Long carritoId = 1L;
        String direccionEnvio = "Calle Principal #123";
        String metodoPago = "Tarjeta de crédito";
        String notas = "Dejar en portería";
        
        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        usuario.setId(1L);
        
        Carrito carrito = new Carrito(usuario);
        carrito.setId(carritoId);
        carrito.setEstado(Carrito.Estado.ACTIVO);
        
        // Productos en carrito
        Producto producto1 = new Producto("Producto 1", "Descripción 1", new BigDecimal("100"), 5);
        producto1.setId(1L);
        Producto producto2 = new Producto("Producto 2", "Descripción 2", new BigDecimal("200"), 10);
        producto2.setId(2L);
        
        CarritoItem item1 = new CarritoItem(carrito, producto1, 2);
        CarritoItem item2 = new CarritoItem(carrito, producto2, 1);
        carrito.setItems(Arrays.asList(item1, item2));
        
        // Mock de Orden creada
        Orden nuevaOrden = new Orden(usuario, direccionEnvio, metodoPago);
        nuevaOrden.setId(1L);
        nuevaOrden.setNotas(notas);
        
        when(carritoRepository.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(ordenRepository.save(any(Orden.class))).thenReturn(nuevaOrden);
        when(productoService.actualizarStock(anyLong(), anyInt())).thenReturn(true);
        
        // Ejecutar
        Orden result = ordenService.crearOrdenDesdeCarrito(carritoId, direccionEnvio, metodoPago, notas);

        // Verificar
        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals(direccionEnvio, result.getDireccionEnvio());
        assertEquals(metodoPago, result.getMetodoPago());
        assertEquals(notas, result.getNotas());
        
        // Verificar llamadas a productoService para actualizar stock
        verify(productoService).actualizarStock(eq(1L), eq(2));
        verify(productoService).actualizarStock(eq(2L), eq(1));
        
        // Verificar que el carrito se marcó como procesado
        ArgumentCaptor<Carrito> carritoCaptor = ArgumentCaptor.forClass(Carrito.class);
        verify(carritoRepository).save(carritoCaptor.capture());
        assertEquals(Carrito.Estado.PROCESADO, carritoCaptor.getValue().getEstado());
    }

    @Test
    void testActualizarEstadoOrden() {
        // Preparar
        Long ordenId = 1L;
        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        
        Orden orden = new Orden(usuario, "Dirección", "Tarjeta");
        orden.setId(ordenId);
        orden.setEstado(Orden.Estado.PENDIENTE);
        
        when(ordenRepository.findById(ordenId)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        // Ejecutar
        Orden result = ordenService.actualizarEstadoOrden(ordenId, Orden.Estado.PAGADO);

        // Verificar
        assertEquals(Orden.Estado.PAGADO, result.getEstado());
        verify(ordenRepository).save(orden);
    }

    @Test
    void testActualizarEstadoOrdenTransicionInvalida() {
        // Preparar
        Long ordenId = 1L;
        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        
        Orden orden = new Orden(usuario, "Dirección", "Tarjeta");
        orden.setId(ordenId);
        orden.setEstado(Orden.Estado.PENDIENTE);
        
        when(ordenRepository.findById(ordenId)).thenReturn(Optional.of(orden));

        // Ejecutar y verificar
        assertThrows(RuntimeException.class, () -> {
            ordenService.actualizarEstadoOrden(ordenId, Orden.Estado.ENTREGADO);
        });
    }

    @Test
    void testCancelarOrden() {
        // Preparar
        Long ordenId = 1L;
        Usuario usuario = new Usuario("John", "Doe", "john@example.com", "password");
        
        Orden orden = new Orden(usuario, "Dirección", "Tarjeta");
        orden.setId(ordenId);
        orden.setEstado(Orden.Estado.PAGADO);
        
        // Productos en la orden
        Producto producto1 = new Producto("Producto 1", "Descripción 1", new BigDecimal("100"), 3);
        producto1.setId(1L);
        DetalleOrden detalle1 = new DetalleOrden(orden, producto1, 2);
        orden.addDetalle(detalle1);
        
        when(ordenRepository.findById(ordenId)).thenReturn(Optional.of(orden));
        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        // Ejecutar
        ordenService.cancelarOrden(ordenId);

        // Verificar
        assertEquals(Orden.Estado.CANCELADO, orden.getEstado());
        verify(productoService).actualizarStock(eq(1L), eq(-2)); // Devolver stock
        verify(ordenRepository).save(orden);
    }
}