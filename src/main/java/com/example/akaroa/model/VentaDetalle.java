package com.example.akaroa.model;

import jakarta.persistence.*;

@Entity
@Table(name="ventas_detalles")
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "idVenta")
    private Venta idVenta;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto idProducto;

    private Integer cantidad;
    private Double precioUnitario;

    // Getters and Setters
    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Venta getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Venta venta) {
        this.idVenta = venta;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto producto) {
        this.idProducto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
