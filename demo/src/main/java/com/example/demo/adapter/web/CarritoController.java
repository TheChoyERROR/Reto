package com.example.demo.adapter.web;

import com.example.demo.application.CarritoService;
import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.CarritoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    @Autowired
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> getCarritoActivo(@PathVariable Long usuarioId) {
        return carritoService.getCarritoActivo(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> crearCarrito(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(carritoService.crearCarrito(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{carritoId}/productos")
    public ResponseEntity<CarritoItem> agregarProducto(@PathVariable Long carritoId,
                                                      @RequestParam Long productoId,
                                                      @RequestParam Integer cantidad) {
        try {
            return ResponseEntity.ok(carritoService.agregarProducto(carritoId, productoId, cantidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("error-message", e.getMessage())
                    .build();
        }
    }

    @PutMapping("/{carritoId}/items/{itemId}")
    public ResponseEntity<CarritoItem> actualizarCantidad(@PathVariable Long carritoId,
                                                         @PathVariable Long itemId,
                                                         @RequestParam Integer cantidad) {
        try {
            CarritoItem item = carritoService.actualizarCantidad(carritoId, itemId, cantidad);
            return item != null ? 
                   ResponseEntity.ok(item) : 
                   ResponseEntity.ok().build(); // Si se eliminó el ítem por cantidad 0
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .header("error-message", e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{carritoId}/items/{itemId}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long carritoId, 
                                               @PathVariable Long itemId) {
        try {
            carritoService.eliminarProducto(carritoId, itemId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{carritoId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long carritoId) {
        try {
            carritoService.vaciarCarrito(carritoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{carritoId}/procesar")
    public ResponseEntity<Void> procesarCarrito(@PathVariable Long carritoId) {
        try {
            carritoService.procesarCarrito(carritoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{carritoId}/abandonar")
    public ResponseEntity<Void> abandonarCarrito(@PathVariable Long carritoId) {
        try {
            carritoService.abandonarCarrito(carritoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}