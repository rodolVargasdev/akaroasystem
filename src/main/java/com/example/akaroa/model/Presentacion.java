package com.example.akaroa.model;

import jakarta.persistence.*;

@Entity
@Table(name="presentaciones")
public class Presentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPresentacion;

    private String descripcion;

    // Getters and Setters
    public Integer getIdPresentacion() {
        return idPresentacion;
    }

    public void setIdPresentacion(Integer idPresentacion) {
        this.idPresentacion = idPresentacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
