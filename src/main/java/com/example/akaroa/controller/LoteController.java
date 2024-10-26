package com.example.akaroa.controller;

import com.example.akaroa.model.Lote;
import com.example.akaroa.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/lots")
public class LoteController {

    @Autowired
    private LoteService loteService;

    @GetMapping
    public List<Lote> getAllLotes() {
        return loteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> getLoteById(@PathVariable Integer id) {
        Optional<Lote> lote = loteService.findById(id);
        return lote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Lote createLote(@RequestBody Lote lote) {
        return loteService.save(lote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> updateLote(@PathVariable Integer id, @RequestBody Lote loteDetails) {
        Optional<Lote> lote = loteService.findById(id);
        if (lote.isPresent()) {
            Lote updatedLote = lote.get();
            updatedLote.setFechaVencimiento(loteDetails.getFechaVencimiento());
            updatedLote.setIdProveedor(loteDetails.getIdProveedor());
            updatedLote.setCantidadProducto(loteDetails.getCantidadProducto());

            return ResponseEntity.ok(loteService.save(updatedLote));

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLote(@PathVariable Integer id) {
        loteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
