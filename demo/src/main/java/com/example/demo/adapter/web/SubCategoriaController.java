package com.example.demo.adapter.web;

import com.example.demo.domain.model.SubCategoria;
import com.example.demo.domain.model.Categoria;
import com.example.demo.adapter.persistence.JpaCategoriaRepository;
import com.example.demo.adapter.persistence.JpaSubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategorias")
public class SubCategoriaController {

    @Autowired
    private JpaCategoriaRepository categoriaRepository;

    @Autowired
    private JpaSubCategoriaRepository subCategoriaRepository;

    /**
     * Crea una nueva subcategoría asociada a una categoría existente.
     * Se espera un JSON:
     * {
     *   "categoriaId": 1,
     *   "nombre": "NombreSubCategoria"
     * }
     */
    @PostMapping
    public ResponseEntity<?> createSubCategoria(@RequestBody SubCategoriaPayload payload) {
        Categoria categoria = categoriaRepository.findById(payload.getCategoriaId()).orElse(null);
        if (categoria == null) {
            return ResponseEntity.badRequest().body("Categoría no encontrada");
        }
        SubCategoria subCategoria = new SubCategoria(payload.getNombre(), categoria);
        categoria.addSubCategoria(subCategoria);
        categoriaRepository.save(categoria);
        return ResponseEntity.ok(subCategoria);
    }


    @GetMapping
    public ResponseEntity<List<SubCategoria>> listSubCategorias() {
        List<SubCategoria> subCategorias = subCategoriaRepository.findAll();
        return ResponseEntity.ok(subCategorias);
    }

    /**
     * Actualiza una subcategoría existente.
     * Se espera un JSON en el body con los nuevos datos
     * Ejemplo:
     * {
     *   "nombre": "NuevoNombre",
     *   "categoriaId": 2 // opcional, para cambiar la categoría asociada
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubCategoria(@PathVariable Long id, @RequestBody SubCategoriaPayload payload) {
        SubCategoria subCategoria = subCategoriaRepository.findById(id).orElse(null);
        if (subCategoria == null) {
            return ResponseEntity.badRequest().body("SubCategoría no encontrada");
        }
        subCategoria.setNombre(payload.getNombre());

        // Cambiar la categoría asociada si se envía un categoriaId diferente
        if (payload.getCategoriaId() != null && !payload.getCategoriaId().equals(subCategoria.getCategoria().getId())) {
            Categoria nuevaCategoria = categoriaRepository.findById(payload.getCategoriaId()).orElse(null);
            if (nuevaCategoria == null) {
                return ResponseEntity.badRequest().body("Nueva categoría no encontrada");
            }
            // Remueve la subcategoría de la categoría antigua
            Categoria categoriaAntigua = subCategoria.getCategoria();
            categoriaAntigua.removeSubCategoria(subCategoria);
            categoriaRepository.save(categoriaAntigua);

            // Agrega la subcategoría a la nueva categoría
            nuevaCategoria.addSubCategoria(subCategoria);
            categoriaRepository.save(nuevaCategoria);
        } else {
            // Solo actualiza el nombre
            subCategoriaRepository.save(subCategoria);
        }
        return ResponseEntity.ok(subCategoria);
    }

    /**
     * Elimina una subcategoría por su id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubCategoria(@PathVariable Long id) {
        SubCategoria subCategoria = subCategoriaRepository.findById(id).orElse(null);
        if (subCategoria == null) {
            return ResponseEntity.badRequest().body("SubCategoría no encontrada");
        }
        // Remover la subcategoría de su categoría para mantener la relación
        Categoria categoria = subCategoria.getCategoria();
        if (categoria != null) {
            categoria.removeSubCategoria(subCategoria);
            categoriaRepository.save(categoria);
        }
        subCategoriaRepository.deleteById(id);
        return ResponseEntity.ok("SubCategoría eliminada correctamente");
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