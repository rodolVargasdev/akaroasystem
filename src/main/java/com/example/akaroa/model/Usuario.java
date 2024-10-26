package com.example.akaroa.model;

import jakarta.persistence.*;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String nombreUsuario;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol idRol;

    // Getters and Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer id_usuario) {
        this.idUsuario = id_usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombre_usuario) {
        this.nombreUsuario = nombre_usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getIdRol() {
        return idRol;
    }

    public void setIdRol(Rol role) {
        this.idRol = role;
    }
}
