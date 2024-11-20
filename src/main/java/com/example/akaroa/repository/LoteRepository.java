package com.example.akaroa.repository;

import com.example.akaroa.model.Lote;
import com.example.akaroa.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    List<Lote> findByProducto(Producto producto);
}
