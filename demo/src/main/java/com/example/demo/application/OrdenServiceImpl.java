package com.example.demo.application;

import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.CarritoItem;
import com.example.demo.domain.model.DetalleOrden;
import com.example.demo.domain.model.Orden;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.CarritoRepositoryPort;
import com.example.demo.domain.port.DetalleOrdenRepositoryPort;
import com.example.demo.domain.port.OrdenRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepositoryPort ordenRepositoryPort;
    private final DetalleOrdenRepositoryPort detalleOrdenRepositoryPort;
    private final CarritoRepositoryPort carritoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final ProductoServiceImpl productoService;

    @Autowired
    public OrdenServiceImpl(OrdenRepositoryPort ordenRepositoryPort,
                          DetalleOrdenRepositoryPort detalleOrdenRepositoryPort,
                          CarritoRepositoryPort carritoRepositoryPort,
                          UsuarioRepositoryPort usuarioRepositoryPort,
                          ProductoServiceImpl productoService) {
        this.ordenRepositoryPort = ordenRepositoryPort;
        this.detalleOrdenRepositoryPort = detalleOrdenRepositoryPort;
        this.carritoRepositoryPort = carritoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.productoService = productoService;
    }

    @Override
    public List<Orden> getAllOrdenes() {
        return ordenRepositoryPort.findAll();
    }

    @Override
    public Optional<Orden> getOrdenById(Long id) {
        return ordenRepositoryPort.findById(id);
    }

    @Override
    public Optional<Orden> getOrdenByNumero(String numeroOrden) {
        return ordenRepositoryPort.findByNumeroOrden(numeroOrden);
    }

    @Override
    public List<Orden> getOrdenesByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ordenRepositoryPort.findByUsuario(usuario);
    }

    @Override
    @Transactional
    public Orden crearOrdenDesdeCarrito(Long carritoId, String direccionEnvio, String metodoPago, String notas) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        if (carrito.getEstado() != Carrito.Estado.ACTIVO) {
            throw new RuntimeException("El carrito no está activo");
        }
        
        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }
        
        // Crear la orden
        Orden orden = new Orden(carrito.getUsuario(), direccionEnvio, metodoPago);
        orden.setNotas(notas);
        orden = ordenRepositoryPort.save(orden);
        
        // Transferir items del carrito a la orden
        for (CarritoItem item : carrito.getItems()) {
            // Verificar stock disponible
            if (!productoService.actualizarStock(item.getProducto().getId(), item.getCantidad())) {
                // Si no hay stock suficiente, revertir la operación
                throw new RuntimeException("No hay suficiente stock para el producto: " + item.getProducto().getNombre());
            }
            
            // Crear detalle de orden
            DetalleOrden detalle = new DetalleOrden(orden, item.getProducto(), item.getCantidad());
            orden.addDetalle(detalle);
        }
        
        // Guardar la orden con sus detalles
        orden = ordenRepositoryPort.save(orden);
        
        // Marcar el carrito como procesado
        carrito.setEstado(Carrito.Estado.PROCESADO);
        carritoRepositoryPort.save(carrito);
        
        return orden;
    }

    @Override
    @Transactional
    public Orden actualizarEstadoOrden(Long id, Orden.Estado nuevoEstado) {
        Orden orden = ordenRepositoryPort.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
            
        // Verificar que el cambio de estado sea válido
        if (!esTransicionEstadoValida(orden.getEstado(), nuevoEstado)) {
            throw new RuntimeException("Transición de estado no válida");
        }
        
        orden.setEstado(nuevoEstado);
        return ordenRepositoryPort.save(orden);
    }

    @Override
    @Transactional
    public void cancelarOrden(Long id) {
        Orden orden = ordenRepositoryPort.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
            
        // Solo se pueden cancelar órdenes pendientes o pagadas
        if (orden.getEstado() != Orden.Estado.PENDIENTE && orden.getEstado() != Orden.Estado.PAGADO) {
            throw new RuntimeException("No se puede cancelar la orden en estado: " + orden.getEstado());
        }
        
        // Devolver stock a los productos
        for (DetalleOrden detalle : orden.getDetalles()) {
            // Incrementar el stock del producto
            productoService.actualizarStock(detalle.getProducto().getId(), -detalle.getCantidad());
        }
        
        orden.setEstado(Orden.Estado.CANCELADO);
        ordenRepositoryPort.save(orden);
    }
    
    // Método auxiliar para validar transiciones de estado
    private boolean esTransicionEstadoValida(Orden.Estado estadoActual, Orden.Estado nuevoEstado) {
        // Implementar lógica para validar transiciones permitidas
        // Por ejemplo:
        switch (estadoActual) {
            case PENDIENTE:
                // De pendiente solo puede pasar a pagado o cancelado
                return nuevoEstado == Orden.Estado.PAGADO || nuevoEstado == Orden.Estado.CANCELADO;
            case PAGADO:
                // De pagado solo puede pasar a enviado o cancelado
                return nuevoEstado == Orden.Estado.ENVIADO || nuevoEstado == Orden.Estado.CANCELADO;
            case ENVIADO:
                // De enviado solo puede pasar a entregado
                return nuevoEstado == Orden.Estado.ENTREGADO;
            case ENTREGADO:
            case CANCELADO:
                // No se puede cambiar el estado una vez entregado o cancelado
                return false;
            default:
                return false;
        }
    }
}