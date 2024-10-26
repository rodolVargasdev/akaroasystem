package com.example.akaroa.model;

import jakarta.persistence.*;

@Entity
@Table(name="unidades_medida")
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUnidadMedida;

    private String descripcion;

    // Getters and Setters
    public Integer getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(Integer idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
