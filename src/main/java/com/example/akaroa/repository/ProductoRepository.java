package com.example.akaroa.repository;

import com.example.akaroa.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);
}
