package com.example.akaroa.model;

import jakarta.persistence.*;

@Entity
@Table(name="roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;
    @Column(unique=true)
    private String nombreRole;

    // Getters and Setters
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRole) {
        this.idRol = idRole;
    }

    public String getNombreRole() {
        return nombreRole;
    }

    public void setNombreRole(String nombreRole) {
        this.nombreRole = nombreRole;
    }
}
