package com.example.akaroa.service;

import com.example.akaroa.model.Usuario;
import com.example.akaroa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Metodo para buscar los usuarios
//    public List<Usuario> buscar(String searchTerm) {
//        if (searchTerm == null || searchTerm.isEmpty()) {
//            return usuarioRepository.findAll(); // retorna todos los usuarios si no hay termino de busqueda
//        }
//        return usuarioRepository.findByNombreCompletoContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm);
//    }

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

//    public Usuario save(Usuario usuario) {
//        return usuarioRepository.save(usuario);
//    }

    // Metodo para guardar o registrar un nuevo usuario
    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // Metodo de email en uso
    public boolean existePorEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
