package com.example.demo.application;

import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepositoryPort.findAll();
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepositoryPort.findById(id);
    }

    @Override
    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepositoryPort.findByEmail(email);
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        // Aquí se podría agregar lógica para encriptar la contraseña antes de guardar el usuario
        return usuarioRepositoryPort.save(usuario);
    }

    @Override
    public Usuario updateUsuario(Long id, Usuario usuario) {
        return usuarioRepositoryPort.findById(id)
            .map(existing -> {
                usuario.setId(id);
                // Mantiene la fecha de registro original
                usuario.setFechaRegistro(existing.getFechaRegistro());
                // Aquí se podría manejar la lógica para no actualizar la contraseña si viene vacía
                if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                    usuario.setPassword(existing.getPassword());
                } else {
                    // Aquí se podría agregar lógica para encriptar la nueva contraseña
                }
                return usuarioRepositoryPort.save(usuario);
            }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepositoryPort.deleteById(id);
    }
}