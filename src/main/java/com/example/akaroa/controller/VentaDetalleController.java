package com.example.akaroa.controller;

import com.example.akaroa.model.VentaDetalle;
import com.example.akaroa.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas-detalles")
public class VentaDetalleController {

    @Autowired
    private VentaDetalleService ventaDetalleService;

    @GetMapping
    public List<VentaDetalle> getAllVentaDetalles() {
        return ventaDetalleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDetalle> getVentaDetalleById(@PathVariable Integer id) {
        Optional<VentaDetalle> ventaDetalle = ventaDetalleService.findById(id);
        return ventaDetalle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public VentaDetalle createVentaDetalle(@RequestBody VentaDetalle ventaDetalle) {
        return ventaDetalleService.save(ventaDetalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDetalle> updateVentaDetalle(@PathVariable Integer id, @RequestBody VentaDetalle ventaDetalleDetails) {
        Optional<VentaDetalle> ventaDetalle = ventaDetalleService.findById(id);
        if (ventaDetalle.isPresent()) {
            VentaDetalle updatedVentaDetalle = ventaDetalle.get();
            updatedVentaDetalle.setCantidad(ventaDetalleDetails.getCantidad());
            updatedVentaDetalle.setPrecioUnitario(ventaDetalleDetails.getPrecioUnitario());
            updatedVentaDetalle.setProducto(ventaDetalleDetails.getProducto());

            return ResponseEntity.ok(ventaDetalleService.save(updatedVentaDetalle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVentaDetalle(@PathVariable Integer id) {
        ventaDetalleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
