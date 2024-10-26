package com.example.akaroa.controller;

import com.example.akaroa.model.Reporte;
import com.example.akaroa.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/reports")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public List<Reporte> getAllReportes() {
        return reporteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getReporteById(@PathVariable Integer id) {
        Optional<Reporte> reporte = reporteService.findById(id);
        return reporte.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reporte createReporte(@RequestBody Reporte reporte) {
        return reporteService.save(reporte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> updateReporte(@PathVariable Integer id, @RequestBody Reporte reporteDetails) {
        Optional<Reporte> reporte = reporteService.findById(id);
        if (reporte.isPresent()) {
            Reporte updatedReporte = reporte.get();
            updatedReporte.setTipoReporte(reporteDetails.getTipoReporte());
            updatedReporte.setContenido(reporteDetails.getContenido());
            updatedReporte.setIdUsuario(reporteDetails.getIdUsuario());
            updatedReporte.setFechaGeneracion(reporteDetails.getFechaGeneracion());

            return ResponseEntity.ok(reporteService.save(updatedReporte));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Integer id) {
        reporteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
