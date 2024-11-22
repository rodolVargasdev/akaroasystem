    package com.example.akaroa.model;

    import jakarta.persistence.*;
    import org.springframework.format.annotation.DateTimeFormat;

    import java.util.Date;

    @Entity
    @Table(name="lotes")
    public class Lote {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idLote;

        @Temporal(TemporalType.DATE)
        @DateTimeFormat(pattern = "yyyy-MM-dd") // Asegúrate de usar el mismo formato que el del formulario
        private Date fechaVencimiento;

        @Temporal(TemporalType.DATE)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date fechaRecepcion;

        private Integer cantidadProducto;

        @ManyToOne
        @JoinColumn(name = "idProveedor")
        private Proveedor idProveedor;

        @ManyToOne
        @JoinColumn(name = "idProducto")
        private Producto idProducto;


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

        public Integer getCantidadProducto() { return cantidadProducto; }

        public String getProveedor(){
            return idProveedor.getNombreProveedor();
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

        public Producto getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(Producto producto) {
            this.idProducto = producto;
        }


        public Date getFechaRecepcion() {
            return fechaRecepcion;
        }

        public void setFechaRecepcion(Date fechaRecepcion) {
            this.fechaRecepcion = fechaRecepcion;
        }
    }
