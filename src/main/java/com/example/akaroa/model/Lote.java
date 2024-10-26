package com.example.akaroa.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="lotes")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLote;

    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;

    private Integer cantidadProducto;

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor idProveedor;

    // Getters and Setters
    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(Integer cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor proveedor) {
        this.idProveedor = proveedor;
    }
}
