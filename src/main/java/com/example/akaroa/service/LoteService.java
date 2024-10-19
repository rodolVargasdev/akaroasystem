package com.example.akaroa.service;

import com.example.akaroa.model.Lote;
import com.example.akaroa.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

    public List<Lote> findAll() {
        return loteRepository.findAll();
    }

    public Optional<Lote> findById(Integer id) {
        return loteRepository.findById(id);
    }

    public Lote save(Lote lote) {
        return loteRepository.save(lote);
    }

    public void deleteById(Integer id) {
        loteRepository.deleteById(id);
    }
}
