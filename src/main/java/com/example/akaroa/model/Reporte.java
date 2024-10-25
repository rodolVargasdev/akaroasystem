package com.example.akaroa.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReporte;

    private String tipoReporte;

    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;

    @Lob
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;

    // Getters and Setters
    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario usuario) {
        this.idUsuario = usuario;
    }
}
