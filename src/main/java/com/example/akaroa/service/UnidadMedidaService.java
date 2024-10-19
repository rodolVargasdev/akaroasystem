package com.example.akaroa.service;

import com.example.akaroa.model.UnidadMedida;
import com.example.akaroa.repository.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaService {

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    public List<UnidadMedida> findAll() {
        return unidadMedidaRepository.findAll();
    }

    public Optional<UnidadMedida> findById(Integer id) {
        return unidadMedidaRepository.findById(id);
    }

    public UnidadMedida save(UnidadMedida unidadMedida) {
        return unidadMedidaRepository.save(unidadMedida);
    }

    public void deleteById(Integer id) {
        unidadMedidaRepository.deleteById(id);
    }
}
