package com.example.akaroa.repository;

import com.example.akaroa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Verificar que exista un usuario
    Optional<Usuario> findByEmail(String email);

    // Encontrar usuario por username
    Optional <Usuario> findByNombreUsuario(String nombreUsuario);

    List<Usuario> findByNombreCompletoContainingIgnoreCaseOrEmailContainingIgnoreCase(String nombre, String email);

}
