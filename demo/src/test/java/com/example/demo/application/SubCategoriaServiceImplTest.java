package com.example.demo.application;

import com.example.demo.domain.model.SubCategoria;
import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.port.SubCategoriaRepositoryPort;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubCategoriaServiceImplTest {

    private SubCategoriaRepositoryPort subCategoriaRepository;
    private CategoriaRepositoryPort categoriaRepository;
    private SubCategoriaService service;

    @BeforeEach
    public void setUp() {
        subCategoriaRepository = Mockito.mock(SubCategoriaRepositoryPort.class);
        categoriaRepository = Mockito.mock(CategoriaRepositoryPort.class);
        service = new SubCategoriaServiceImpl(subCategoriaRepository, categoriaRepository);
    }

    @Test
    public void testCreateSubCategoria() {
        Categoria categoria = new Categoria("Test Categoria");
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        SubCategoria subCategoria = new SubCategoria("New Subcategoria", categoria);
        when(subCategoriaRepository.save(any(SubCategoria.class))).thenReturn(subCategoria);

        SubCategoria result = service.createSubCategoria(1L, "New Subcategoria");

        assertNotNull(result);
        assertEquals("New Subcategoria", result.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(subCategoriaRepository, times(1)).save(any(SubCategoria.class));
    }

    @Test
    public void testUpdateSubCategoriaNotFound() {
        when(subCategoriaRepository.findById(100L)).thenReturn(Optional.empty());
        Optional<SubCategoria> result = service.updateSubCategoria(100L, 1L, "Updated Name");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateSubCategoria() {
        // Configuramos la categoria original y otra para cambio
        Categoria categoriaOriginal = new Categoria("Original Categoria");
        categoriaOriginal.setId(1L);
        Categoria nuevaCategoria = new Categoria("Nueva Categoria");
        nuevaCategoria.setId(2L);
        SubCategoria subCategoria = new SubCategoria("Old Name", categoriaOriginal);
        subCategoria.setId(1L);
        categoriaOriginal.addSubCategoria(subCategoria);

        when(subCategoriaRepository.findById(1L)).thenReturn(Optional.of(subCategoria));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(nuevaCategoria));
        when(subCategoriaRepository.save(any(SubCategoria.class))).thenReturn(subCategoria);

        Optional<SubCategoria> result = service.updateSubCategoria(1L, 2L, "Updated Name");
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getNombre());
        assertEquals(nuevaCategoria, result.get().getCategoria());
        verify(categoriaRepository).findById(2L);
        verify(subCategoriaRepository).save(any(SubCategoria.class));
    }

    @Test
    public void testDeleteSubCategoria() {
        Categoria categoria = new Categoria("Test Categoria");
        categoria.setId(1L);
        SubCategoria subCategoria = new SubCategoria("Subcategoria", categoria);
        subCategoria.setId(1L);
        categoria.addSubCategoria(subCategoria);

        when(subCategoriaRepository.findById(1L)).thenReturn(Optional.of(subCategoria));
        doNothing().when(subCategoriaRepository).deleteById(1L);

        service.deleteSubCategoria(1L);
        verify(subCategoriaRepository).deleteById(1L);
        // Se verifica que se haya eliminado la referencia en la lista
        assertFalse(categoria.getSubCategorias().contains(subCategoria));
    }
}