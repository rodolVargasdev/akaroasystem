package com.example.akaroa.controller;

import com.example.akaroa.model.Proveedor;
import com.example.akaroa.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/provendered")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> getAllProveedores() {
        return proveedorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Integer id) {
        Optional<Proveedor> proveedor = proveedorService.findById(id);
        return proveedor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Proveedor createProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.save(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@PathVariable Integer id, @RequestBody Proveedor proveedorDetails) {
        Optional<Proveedor> proveedor = proveedorService.findById(id);
        if (proveedor.isPresent()) {
            Proveedor updatedProveedor = proveedor.get();
            updatedProveedor.setNombreProveedor(proveedorDetails.getNombreProveedor());
            updatedProveedor.setDireccion(proveedorDetails.getDireccion());
            updatedProveedor.setTelefono(proveedorDetails.getTelefono());

            return ResponseEntity.ok(proveedorService.save(updatedProveedor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Integer id) {
        proveedorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
