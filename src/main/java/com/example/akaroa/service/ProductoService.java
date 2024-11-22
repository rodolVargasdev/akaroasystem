package com.example.akaroa.service;

import com.example.akaroa.model.Producto;
import com.example.akaroa.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteById(Integer id) {
        productoRepository.deleteById(id);
    }

    public void delete(Producto producto) {
        productoRepository.delete(producto);
    }

    public void actualizarProducto(Producto producto) {
        productoRepository.save(producto);
    }

}
