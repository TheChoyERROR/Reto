package com.example.demo.application;

import com.example.demo.domain.model.Categoria;
import com.example.demo.domain.port.CategoriaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    private CategoriaRepositoryPort repository;
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CategoriaRepositoryPort.class);
        categoriaService = new CategoriaServiceImpl(repository);
    }

    @Test
    void testGetAllCategorias() {
        Categoria cat1 = new Categoria("Categoria 1");
        cat1.setId(1L);
        Categoria cat2 = new Categoria("Categoria 2");
        cat2.setId(2L);

        when(repository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<Categoria> result = categoriaService.getAllCategorias();
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }
}