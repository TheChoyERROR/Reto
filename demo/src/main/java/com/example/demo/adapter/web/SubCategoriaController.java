package com.example.demo.adapter.web;

import com.example.demo.application.SubCategoriaService;
import com.example.demo.domain.model.SubCategoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subcategorias")
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }

    @PostMapping
    public ResponseEntity<?> createSubCategoria(@RequestBody SubCategoriaPayload payload) {
        try {
            SubCategoria subCategoria = subCategoriaService.createSubCategoria(
                    payload.getCategoriaId(), payload.getNombre());
            return ResponseEntity.ok(subCategoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SubCategoria>> listSubCategorias() {
        List<SubCategoria> list = subCategoriaService.listSubCategorias();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubCategoria(@PathVariable Long id, @RequestBody SubCategoriaPayload payload) {
        try {
            return subCategoriaService.updateSubCategoria(id, payload.getCategoriaId(), payload.getNombre())
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.badRequest().body("SubCategoría no encontrada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubCategoria(@PathVariable Long id) {
        try {
            subCategoriaService.deleteSubCategoria(id);
            return ResponseEntity.ok("SubCategoría eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

class SubCategoriaPayload {
    private Long categoriaId;
    private String nombre;

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}