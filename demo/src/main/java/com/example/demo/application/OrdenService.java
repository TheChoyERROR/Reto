package com.example.demo.application;

import com.example.demo.domain.model.Orden;
import java.util.List;
import java.util.Optional;

public interface OrdenService {
    List<Orden> getAllOrdenes();
    Optional<Orden> getOrdenById(Long id);
    Optional<Orden> getOrdenByNumero(String numeroOrden);
    List<Orden> getOrdenesByUsuario(Long usuarioId);
    Orden crearOrdenDesdeCarrito(Long carritoId, String direccionEnvio, String metodoPago, String notas);
    Orden actualizarEstadoOrden(Long id, Orden.Estado nuevoEstado);
    void cancelarOrden(Long id);
}