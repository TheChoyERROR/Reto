package com.example.demo.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    private LocalDateTime fechaCreacion;
    
    @Enumerated(EnumType.STRING)
    private Estado estado;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();
    
    public enum Estado {
        ACTIVO, PROCESADO, ABANDONADO
    }
    
    public Carrito() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = Estado.ACTIVO;
    }
    
    public Carrito(Usuario usuario) {
        this();
        this.usuario = usuario;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public List<CarritoItem> getItems() {
        return items;
    }
    
    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }
    
    // Método para agregar un item al carrito
    public void addItem(CarritoItem item) {
        items.add(item);
        item.setCarrito(this);
    }
    
    // Método para eliminar un item del carrito
    public void removeItem(CarritoItem item) {
        items.remove(item);
        item.setCarrito(null);
    }
}