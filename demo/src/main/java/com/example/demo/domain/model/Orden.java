package com.example.demo.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String numeroOrden;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    private LocalDateTime fechaPedido;
    
    @Enumerated(EnumType.STRING)
    private Estado estado;
    
    private BigDecimal total;
    
    private String direccionEnvio;
    private String metodoPago;
    private String notas;
    
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();
    
    public enum Estado {
        PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    }
    
    public Orden() {
        this.fechaPedido = LocalDateTime.now();
        this.estado = Estado.PENDIENTE;
        this.total = BigDecimal.ZERO;
    }
    
    public Orden(Usuario usuario, String direccionEnvio, String metodoPago) {
        this();
        this.usuario = usuario;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.numeroOrden = generarNumeroOrden();
    }
    
    private String generarNumeroOrden() {
        return "ORD-" + System.currentTimeMillis();
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumeroOrden() {
        return numeroOrden;
    }
    
    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }
    
    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    
    public Estado getEstado() {
        return estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public String getDireccionEnvio() {
        return direccionEnvio;
    }
    
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public List<DetalleOrden> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleOrden> detalles) {
        this.detalles = detalles;
    }
    
    // Método para agregar un detalle a la orden
    public void addDetalle(DetalleOrden detalle) {
        detalles.add(detalle);
        detalle.setOrden(this);
        recalcularTotal();
    }
    
    // Método para eliminar un detalle de la orden
    public void removeDetalle(DetalleOrden detalle) {
        detalles.remove(detalle);
        detalle.setOrden(null);
        recalcularTotal();
    }
    
    // Método para recalcular el total de la orden
    private void recalcularTotal() {
        total = detalles.stream()
                .map(DetalleOrden::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}