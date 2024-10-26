package com.example.akaroa.service;

import com.example.akaroa.model.VentaDetalle;
import com.example.akaroa.repository.VentaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaDetalleService {

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    public List<VentaDetalle> findAll() {
        return ventaDetalleRepository.findAll();
    }

    public Optional<VentaDetalle> findById(Integer id) {
        return ventaDetalleRepository.findById(id);
    }

    public VentaDetalle save(VentaDetalle ventaDetalle) {
        return ventaDetalleRepository.save(ventaDetalle);
    }

    public void deleteById(Integer id) {
        ventaDetalleRepository.deleteById(id);
    }
}
