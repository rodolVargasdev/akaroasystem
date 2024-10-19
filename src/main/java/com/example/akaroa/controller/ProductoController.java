package com.example.akaroa.controller;

import com.example.akaroa.model.Producto;
import com.example.akaroa.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody Producto productoDetails) {
        Optional<Producto> producto = productoService.findById(id);
        if (producto.isPresent()) {
            Producto updatedProducto = producto.get();
            updatedProducto.setPrecio(productoDetails.getPrecio());
            updatedProducto.setDescripcion(productoDetails.getDescripcion());
            updatedProducto.setCategoria(productoDetails.getCategoria());
            updatedProducto.setLote(productoDetails.getLote());
            updatedProducto.setPresentacion(productoDetails.getPresentacion());
            updatedProducto.setNombreProducto(productoDetails.getNombreProducto());
            updatedProducto.setImagenUrl(productoDetails.getImagenUrl());
            return ResponseEntity.ok(productoService.save(updatedProducto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
