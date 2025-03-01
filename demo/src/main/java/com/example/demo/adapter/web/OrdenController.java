package com.example.demo.adapter.web;

import com.example.demo.application.OrdenService;
import com.example.demo.domain.model.Orden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    @Autowired
    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public ResponseEntity<List<Orden>> getAllOrdenes() {
        return ResponseEntity.ok(ordenService.getAllOrdenes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrdenById(@PathVariable Long id) {
        return ordenService.getOrdenById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<Orden> getOrdenByNumero(@PathVariable String numeroOrden) {
        return ordenService.getOrdenByNumero(numeroOrden)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Orden>> getOrdenesByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.getOrdenesByUsuario(usuarioId));
    }

    @PostMapping("/carrito/{carritoId}")
    public ResponseEntity<Orden> crearOrdenDesdeCarrito(@PathVariable Long carritoId,
                                                       @RequestBody Map<String, String> datos) {
        try {
            String direccionEnvio = datos.get("direccionEnvio");
            String metodoPago = datos.get("metodoPago");
            String notas = datos.getOrDefault("notas", "");
            
            return ResponseEntity.ok(ordenService.crearOrdenDesdeCarrito(
                carritoId, direccionEnvio, metodoPago, notas));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("error-message", e.getMessage())
                    .build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Orden> actualizarEstadoOrden(@PathVariable Long id,
                                                      @RequestBody Map<String, String> datos) {
        try {
            String estadoStr = datos.get("estado");
            Orden.Estado nuevoEstado = Orden.Estado.valueOf(estadoStr);
            
            return ResponseEntity.ok(ordenService.actualizarEstadoOrden(id, nuevoEstado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .header("error-message", "Estado de orden no válido")
                    .build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("error-message", e.getMessage())
                    .build();
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarOrden(@PathVariable Long id) {
        try {
            ordenService.cancelarOrden(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("error-message", e.getMessage())
                    .build();
        }
    }
}