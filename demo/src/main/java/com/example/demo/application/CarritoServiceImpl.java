package com.example.demo.application;

import com.example.demo.domain.model.Carrito;
import com.example.demo.domain.model.CarritoItem;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.CarritoRepositoryPort;
import com.example.demo.domain.port.CarritoItemRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepositoryPort carritoRepositoryPort;
    private final CarritoItemRepositoryPort carritoItemRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Autowired
    public CarritoServiceImpl(CarritoRepositoryPort carritoRepositoryPort,
                             CarritoItemRepositoryPort carritoItemRepositoryPort,
                             ProductoRepositoryPort productoRepositoryPort,
                             UsuarioRepositoryPort usuarioRepositoryPort) {
        this.carritoRepositoryPort = carritoRepositoryPort;
        this.carritoItemRepositoryPort = carritoItemRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Optional<Carrito> getCarritoActivo(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepositoryPort.findById(usuarioId);
        if (usuario.isPresent()) {
            return carritoRepositoryPort.findByUsuarioAndEstado(usuario.get(), Carrito.Estado.ACTIVO);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Carrito crearCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        // Comprobar si ya existe un carrito activo
        Optional<Carrito> carritoExistente = carritoRepositoryPort.findByUsuarioAndEstado(usuario, Carrito.Estado.ACTIVO);
        if (carritoExistente.isPresent()) {
            return carritoExistente.get();
        }
        
        Carrito carrito = new Carrito(usuario);
        return carritoRepositoryPort.save(carrito);
    }

    @Override
    @Transactional
    public CarritoItem agregarProducto(Long carritoId, Long productoId, Integer cantidad) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        if (carrito.getEstado() != Carrito.Estado.ACTIVO) {
            throw new RuntimeException("El carrito no está activo");
        }
        
        Producto producto = productoRepositoryPort.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
        if (!producto.isActivo() || producto.getStock() < cantidad) {
            throw new RuntimeException("Producto no disponible en la cantidad solicitada");
        }
        
        // Verificar si el producto ya está en el carrito
        Optional<CarritoItem> itemExistente = carritoItemRepositoryPort
            .findByCarritoAndProducto(carrito, producto);
            
        if (itemExistente.isPresent()) {
            // Actualizar cantidad si ya existe
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoItemRepositoryPort.save(item);
        } else {
            // Agregar nuevo item al carrito
            CarritoItem nuevoItem = new CarritoItem(carrito, producto, cantidad);
            carrito.addItem(nuevoItem);
            return carritoItemRepositoryPort.save(nuevoItem);
        }
    }

    @Override
    @Transactional
    public CarritoItem actualizarCantidad(Long carritoId, Long itemId, Integer cantidad) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        if (carrito.getEstado() != Carrito.Estado.ACTIVO) {
            throw new RuntimeException("El carrito no está activo");
        }
        
        CarritoItem item = carritoItemRepositoryPort.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item no encontrado"));
            
        if (!item.getCarrito().getId().equals(carritoId)) {
            throw new RuntimeException("El item no pertenece al carrito especificado");
        }
        
        if (cantidad <= 0) {
            // Si la cantidad es cero o negativa, eliminar el item
            carrito.removeItem(item);
            carritoItemRepositoryPort.deleteById(itemId);
            return null;
        } else {
            // Verificar disponibilidad de stock
            if (item.getProducto().getStock() < cantidad) {
                throw new RuntimeException("No hay suficiente stock disponible");
            }
            
            item.setCantidad(cantidad);
            return carritoItemRepositoryPort.save(item);
        }
    }

    @Override
    @Transactional
    public void eliminarProducto(Long carritoId, Long itemId) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        CarritoItem item = carritoItemRepositoryPort.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item no encontrado"));
            
        if (!item.getCarrito().getId().equals(carritoId)) {
            throw new RuntimeException("El item no pertenece al carrito especificado");
        }
        
        carrito.removeItem(item);
        carritoItemRepositoryPort.deleteById(itemId);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        // Eliminar todos los items del carrito
        List<CarritoItem> items = carritoItemRepositoryPort.findByCarrito(carrito);
        for (CarritoItem item : items) {
            carritoItemRepositoryPort.deleteById(item.getId());
        }
        
        carrito.getItems().clear();
        carritoRepositoryPort.save(carrito);
    }

    @Override
    @Transactional
    public void procesarCarrito(Long carritoId) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        if (carrito.getEstado() != Carrito.Estado.ACTIVO) {
            throw new RuntimeException("El carrito no está activo");
        }
        
        carrito.setEstado(Carrito.Estado.PROCESADO);
        carritoRepositoryPort.save(carrito);
    }

    @Override
    @Transactional
    public void abandonarCarrito(Long carritoId) {
        Carrito carrito = carritoRepositoryPort.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            
        carrito.setEstado(Carrito.Estado.ABANDONADO);
        carritoRepositoryPort.save(carrito);
    }
}