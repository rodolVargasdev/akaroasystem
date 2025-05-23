package com.example.akaroa.repository;

import com.example.akaroa.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNombreClienteContainingIgnoreCase(String nombreCliente);
    boolean existsByCorreo(String correo);
    boolean existsByNombreCliente(String nombreCliente);
    Optional<Cliente> findByCorreo(String correo);

}
