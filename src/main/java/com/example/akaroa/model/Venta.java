package com.example.akaroa.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;

    @Temporal(TemporalType.DATE)
    private Date fechaVenta;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente idCliente;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario idUsuario;

    // Getters and Setters
    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente cliente) {
        this.idCliente = cliente;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario usuario) {
        this.idUsuario = usuario;
    }
}
