package com.example.akaroa.controller;

import com.example.akaroa.model.Cliente;
import com.example.akaroa.model.Producto;
import com.example.akaroa.model.Venta;
import com.example.akaroa.model.VentaDetalle;
import com.example.akaroa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final VentaService ventaService;
    private final VentaDetalleService ventaDetalleService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final ExportService exportService;

    @Autowired
    public ReporteController(VentaService ventaService, VentaDetalleService ventaDetalleService,
                             ProductoService productoService, ClienteService clienteService,
                             ExportService exportService) {
        this.ventaService = ventaService;
        this.ventaDetalleService = ventaDetalleService;
        this.productoService = productoService;
        this.clienteService = clienteService;
        this.exportService = exportService;
    }

    //mostrar la pagina de reportes
//    @GetMapping
//    public String mostrarPaginaReportes() {
//        return "reportes";
//    }

    // mostrar pagina de reporte de ventas
    @GetMapping("/ventas")
    public String reporteVentas(Model model) {
        List<Venta> ventas = ventaService.findAll();
        model.addAttribute("ventas", ventas);
        model.addAttribute("activeTab", "ventas");
        return "reportes";
    }

    // mostrar los detalles de venta
    @GetMapping("/ventas/detalles")
    @ResponseBody
    public List<VentaDetalle> getDetallesVenta(@RequestParam Integer idVenta) {
        return ventaDetalleService.findByVenta(idVenta);
    }

    // exportar reporte de ventas a excel
    @GetMapping("/ventas/export/excel")
    public ResponseEntity<InputStreamResource> exportVentasToExcel() throws IOException {
        List<Venta> ventas = ventaService.findAll();
        ByteArrayInputStream in = exportService.exportVentasToExcel(ventas);

        // Formatear la fecha asegurando compatibilidad en nombre de archivo
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"); // Evita / y :
        String fechaFormateada = ahora.format(formatter);
        String fileName = "Reporte de Ventas " + fechaFormateada + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    // Exportar reporte de ventas a PDF
    @GetMapping("/ventas/export/pdf")
    public ResponseEntity<InputStreamResource> exportVentasToPdf() throws Exception {
        List<Venta> ventas = ventaService.findAll();
        ByteArrayInputStream in = exportService.exportVentasToPdf(ventas);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"); // Evita / y :
        String fechaFormateada = ahora.format(formatter);
        String fileName = "Reporte de Ventas " + fechaFormateada + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    // mostrar el reporte de clientes
    @GetMapping("/clientes")
    public String reporteClientes(Model model) {
        List<Cliente> clientes = clienteService.findAll();
        model.addAttribute("clientes", clientes);
        model.addAttribute("activeTab", "clientes");
        return "reportes";
    }

    // exportar reporte de clientes a excel
    @GetMapping("/clientes/export/excel")
    public ResponseEntity<InputStreamResource> exportClientesToExcel() throws IOException {
        List<Cliente> clientes = clienteService.findAll();

        ByteArrayInputStream in = exportService.exportClientesToExcel(clientes);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"); // Evita / y :
        String fechaFormateada = ahora.format(formatter);
        String fileName = "Reporte de clientes " + fechaFormateada + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    // exportar reporte de clientes a pdf
    @GetMapping("/clientes/export/pdf")
    public ResponseEntity<InputStreamResource> exportClientesToPdf() throws Exception {
        List<Cliente> clientes = clienteService.findAll();

        ByteArrayInputStream in = exportService.exportClientesToPdf(clientes);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"); // Evita / y :
        String fechaFormateada = ahora.format(formatter);
        String fileName = "Reporte de Clientes " + fechaFormateada + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    // mostrar el reporte de productos
    @GetMapping("/productos")
    public String reporteProductos(Model model) {
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        model.addAttribute("activeTab", "productos");
        return "reportes";
    }

    // Exportar reporte de productos a Excel
    @GetMapping("/productos/export/excel")
    public ResponseEntity<InputStreamResource> exportProductosToExcel() throws IOException {
        List<Producto> productos = productoService.findAll();

        ByteArrayInputStream in = exportService.exportProductosToExcel(productos);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"); // Evita / y :
        String fechaFormateada = ahora.format(formatter);
        String fileName = "Reporte de Productos " + fechaFormateada + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    // Exportar reporte de productos a PDF
    @GetMapping("/productos/export/pdf")
    public ResponseEntity<InputStreamResource> exportProductosToPdf() throws Exception {
        List<Producto> productos = productoService.findAll();

        ByteArrayInputStream in = exportService.exportProductosToPdf(productos);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"); // Evita / y :
        String fechaFormateada = ahora.format(formatter);
        String fileName = "Reporte de Productos " + fechaFormateada + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}
