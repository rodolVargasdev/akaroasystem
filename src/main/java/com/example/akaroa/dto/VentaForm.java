package com.example.akaroa.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaForm {
    private Integer clienteId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaVenta;
    private Integer usuarioId;
    private List<VentaDetalleForm> detalles = new ArrayList<>();

    public List<VentaDetalleForm> getDetalles() {return detalles;}
    public void setDetalles(List<VentaDetalleForm> detalles) {this.detalles = detalles;}

    public Integer getClienteId() {return clienteId;}
    public void setClienteId(Integer clienteId) {this.clienteId = clienteId;}

    public Date getFechaVenta() {return fechaVenta;}
    public void setFechaVenta(Date fechaVenta) {this.fechaVenta = fechaVenta;}

    public Integer getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Integer usuarioId) {this.usuarioId = usuarioId;}

    public static class VentaDetalleForm {
        private Integer productoId;
        private Integer cantidad;
        private Double precioUnitario;
        private Integer loteId;

        public Integer getProductoId() {return productoId;}
        public void setProductoId(Integer productoId) {this.productoId = productoId;}

        public Integer getCantidad() {return cantidad;}
        public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}

        public Double getPrecioUnitario() {return precioUnitario;}
        public void setPrecioUnitario(Double precioUnitario) {this.precioUnitario = precioUnitario;}

        public Integer getLoteId() {return loteId;}
        public void setLoteId(Integer loteId) {this.loteId = loteId;}
    }
}
