package com.example.demo.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre, descripcion;

    // Relación OneToMany con SubCategoria
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubCategoria> subCategorias = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Getters, setters y métodos de ayuda
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public List<SubCategoria> getSubCategorias() {
        return subCategorias;
    }
    
    public void setSubCategorias(List<SubCategoria> subCategorias) {
        this.subCategorias = subCategorias;
    }
    
    public void addSubCategoria(SubCategoria subCategoria) {
        subCategorias.add(subCategoria);
        subCategoria.setCategoria(this);
    }
    
    public void removeSubCategoria(SubCategoria subCategoria) {
        subCategorias.remove(subCategoria);
        subCategoria.setCategoria(null);
    }
}