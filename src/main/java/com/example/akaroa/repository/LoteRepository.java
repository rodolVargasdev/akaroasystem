package com.example.akaroa.repository;

import com.example.akaroa.model.Lote;
import com.example.akaroa.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    List<Lote> findByIdProducto(Producto producto);
    // Nuevo método para buscar por nombre de producto
    @Query("SELECT l FROM Lote l JOIN l.idProducto p WHERE p.nombreProducto LIKE %:nombreProducto%")
    List<Lote> findByNombreProductoContaining(@Param("nombreProducto") String nombreProducto);

    @Query("SELECT l FROM Lote l JOIN l.idProducto p WHERE p.nombreProducto LIKE %:nombreProducto% AND l.cantidadProducto > 0")
    List<Lote> findByNombreProductoContainingAndCantidadProductoGreaterThan(@Param("nombreProducto") String nombreProducto);

    List<Lote> findByidProductoOrderByFechaVencimientoAsc(Producto producto);

    List<Lote> findByCantidadProductoGreaterThan(int cantidad);

    List<Lote> findByIdProductoAndCantidadProductoGreaterThan(Producto producto, int cantidad);

    List<Lote> findByIdProductoAndCantidadProductoGreaterThanOrderByFechaVencimientoAsc(Producto producto, int cantidad);
}
