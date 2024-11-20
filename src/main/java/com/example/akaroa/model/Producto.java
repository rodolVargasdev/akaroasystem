package com.example.akaroa.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    private String nombreProducto;
    private Double precio;

    @Lob
    private String descripcion;

    private String imagenUrl;

//    @OneToMany
//    @JoinColumn(name = "idLote")
//    private List<Lote> idLote;

    @OneToOne
    @JoinColumn(name = "idPresentacion")
    private Presentacion idPresentacion;

    @OneToOne
    @JoinColumn(name = "idUnidadMedida")
    private UnidadMedida idUnidadMedida;

    @OneToOne
    @JoinColumn(name = "idCategoria")
    private Categoria idCategoria;

    // Getters and Setters
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }



    public Presentacion getIdPresentacion() {
        return idPresentacion;
    }

    public void setIdPresentacion(Presentacion presentacion) {
        this.idPresentacion = presentacion;
    }

    public UnidadMedida getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(UnidadMedida unidadMedida) {
        this.idUnidadMedida = unidadMedida;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria categoria) {
        this.idCategoria = categoria;
    }
}
