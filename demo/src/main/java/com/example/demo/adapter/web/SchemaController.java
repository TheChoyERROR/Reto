package com.example.demo.adapter.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SchemaController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Endpoint para agregar una nueva columna a la tabla "categoria".
     * Espera en el cuerpo un JSON con:
     * - "columnName": nombre de la columna a agregar
     * - "columnType": tipo de dato de la nueva columna (ejemplo: VARCHAR(100), INTEGER, etc.)
     *
     * Ejemplo:
     * {
     *    "columnName": "nuevoCampo",
     *    "columnType": "VARCHAR(100)"
     * }
     */
    @PostMapping("/addColumn")
    public ResponseEntity<String> addColumn(@RequestBody Map<String, String> payload) {
        String columnName = payload.get("columnName");
        String columnType = payload.get("columnType");
        if (columnName == null || columnType == null) {
            return ResponseEntity.badRequest().body("Debe enviar 'columnName' y 'columnType'");
        }
        try {
            // Construye y ejecuta el comando DDL
            String sql = "ALTER TABLE categoria ADD COLUMN " + columnName + " " + columnType;
            jdbcTemplate.execute(sql);
            return ResponseEntity.ok("Columna '" + columnName + "' agregada correctamente a la tabla 'categoria'");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar la columna: " + e.getMessage());
        }
    }
}