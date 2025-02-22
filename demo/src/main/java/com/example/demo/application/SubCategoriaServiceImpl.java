package com.example.demo.application;

import com.example.demo.domain.model.SubCategoria;
import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.port.SubCategoriaRepositoryPort;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SubCategoriaServiceImpl implements SubCategoriaService {

    private final SubCategoriaRepositoryPort subCategoriaRepository;
    private final CategoriaRepositoryPort categoriaRepository;

    public SubCategoriaServiceImpl(SubCategoriaRepositoryPort subCategoriaRepository,
                                   CategoriaRepositoryPort categoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public SubCategoria createSubCategoria(Long categoriaId, String nombre) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        SubCategoria subCategoria = new SubCategoria(nombre, categoria);
        // Establecemos la relación en ambas direcciones.
        categoria.addSubCategoria(subCategoria);
        return subCategoriaRepository.save(subCategoria);
    }

    @Override
    public List<SubCategoria> listSubCategorias() {
        return subCategoriaRepository.findAll();
    }

    @Override
    public Optional<SubCategoria> updateSubCategoria(Long id, Long categoriaId, String nombre) {
        Optional<SubCategoria> opt = subCategoriaRepository.findById(id);
        if (opt.isPresent()) {
            SubCategoria subCategoria = opt.get();
            subCategoria.setNombre(nombre);
            if (categoriaId != null && !categoriaId.equals(subCategoria.getCategoria().getId())) {
                // Cambiar de categoría
                Categoria nuevaCategoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new RuntimeException("Nueva categoría no encontrada"));
                Categoria antiguaCategoria = subCategoria.getCategoria();
                antiguaCategoria.removeSubCategoria(subCategoria);
                nuevaCategoria.addSubCategoria(subCategoria);
            }
            return Optional.of(subCategoriaRepository.save(subCategoria));
        }
        return Optional.empty();
    }

    @Override
    public void deleteSubCategoria(Long id) {
        SubCategoria subCategoria = subCategoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada"));
        // Remover la relación
        Categoria categoria = subCategoria.getCategoria();
        if (categoria != null) {
            categoria.removeSubCategoria(subCategoria);
        }
        subCategoriaRepository.deleteById(id);
    }
}