package com.example.akaroa.service;

import com.example.akaroa.model.Presentacion;
import com.example.akaroa.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PresentacionService {

    @Autowired
    private PresentacionRepository presentacionRepository;

    public List<Presentacion> findAll() {
        return presentacionRepository.findAll();
    }

    public Optional<Presentacion> findById(Integer id) {
        return presentacionRepository.findById(id);
    }

    public Presentacion save(Presentacion presentacion) {
        return presentacionRepository.save(presentacion);
    }

    public void deleteById(Integer id) {
        presentacionRepository.deleteById(id);
    }
}
