package com.example.akaroa.service;

import com.example.akaroa.model.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExportService {

    private final VentaDetalleService ventaDetalleService;

    @Autowired
    public ExportService(VentaDetalleService ventaDetalleService) {
        this.ventaDetalleService = ventaDetalleService;
    }

    public ByteArrayInputStream exportVentasToExcel(List<Venta> ventas) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ventas");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Crear fila de encabezado principal
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID Venta", "Fecha", "Cliente", "Usuario", "Total Venta"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos principales
            int rowNum = 1;
            for (Venta venta : ventas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(venta.getIdVenta());

                // Formatear fecha
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                row.createCell(1).setCellValue(sdf.format(venta.getFechaVenta()));

                row.createCell(2).setCellValue(venta.getIdCliente() != null ? venta.getIdCliente().getNombreCliente() : "N/A");
                row.createCell(3).setCellValue(venta.getIdUsuario() != null ? venta.getIdUsuario().getNombreCompleto() : "N/A");

                // Calcular total de la venta
                List<VentaDetalle> detalles = ventaDetalleService.findByVenta(venta.getIdVenta());
                double total = detalles.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
                row.createCell(4).setCellValue(total);

                // Agregar detalles como filas adicionales
                if (!detalles.isEmpty()) {
                    // Encabezado de detalles
                    Row detHeaderRow = sheet.createRow(rowNum++);
                    detHeaderRow.createCell(0).setCellValue("Detalles:");
                    CellStyle detHeaderStyle = workbook.createCellStyle();
                    Font detHeaderFont = workbook.createFont();
                    detHeaderFont.setItalic(true);
                    detHeaderStyle.setFont(detHeaderFont);
                    detHeaderRow.getCell(0).setCellStyle(detHeaderStyle);

                    // Encabezados de columnas de detalles
                    Row detColHeaderRow = sheet.createRow(rowNum++);
                    String[] detColumns = {"Producto", "Cantidad", "Precio Unitario", "Subtotal", "Lote"};
                    for (int i = 0; i < detColumns.length; i++) {
                        Cell cell = detColHeaderRow.createCell(i + 1); // Desplazado a la derecha
                        cell.setCellValue(detColumns[i]);
                        cell.setCellStyle(headerStyle);
                    }

                    // Filas de detalles
                    for (VentaDetalle detalle : detalles) {
                        Row detRow = sheet.createRow(rowNum++);
                        detRow.createCell(1).setCellValue(detalle.getIdProducto().getNombreProducto());
                        detRow.createCell(2).setCellValue(detalle.getCantidad());
                        detRow.createCell(3).setCellValue(detalle.getPrecioUnitario());
                        detRow.createCell(4).setCellValue(detalle.getCantidad() * detalle.getPrecioUnitario());
                        detRow.createCell(5).setCellValue(detalle.getLote() != null ? detalle.getLote().getIdLote().toString() : "N/A");
                    }

                    // Espacio entre ventas
                    rowNum++;
                }
            }

            // Autoajustar columnas
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    public ByteArrayInputStream exportVentasToPdf(List<Venta> ventas) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate()); // Horizontal para más espacio
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Título
        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Reporte de Ventas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Formateador de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Venta venta : ventas) {
            // Información principal de la venta
            com.lowagie.text.Font ventaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph ventaInfo = new Paragraph();
            ventaInfo.add(new Chunk("Venta #" + venta.getIdVenta() + " - ", ventaFont));
            ventaInfo.add(new Chunk(sdf.format(venta.getFechaVenta()) + " - ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
            ventaInfo.add(new Chunk("Cliente: " + (venta.getIdCliente() != null ? venta.getIdCliente().getNombreCliente() : "N/A") + " - ", FontFactory.getFont(FontFactory.HELVETICA, 12)));
            ventaInfo.add(new Chunk("Vendedor: " + (venta.getIdUsuario() != null ? venta.getIdUsuario().getNombreCompleto() : "N/A"), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            ventaInfo.setSpacingAfter(10f);
            document.add(ventaInfo);

            // Obtener detalles
            List<VentaDetalle> detalles = ventaDetalleService.findByVenta(venta.getIdVenta());

            if (!detalles.isEmpty()) {
                // Tabla de detalles
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(5f);
                table.setSpacingAfter(15f);

                // Encabezados de tabla
                String[] headers = {"Producto", "Cantidad", "Precio Unitario", "Subtotal", "Lote"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header));
                    cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
                    cell.setPadding(5);
                    table.addCell(cell);
                }

                // Datos de tabla
                double totalVenta = 0;
                for (VentaDetalle detalle : detalles) {
                    table.addCell(detalle.getIdProducto().getNombreProducto());
                    table.addCell(String.valueOf(detalle.getCantidad()));
                    table.addCell(String.format("$%.2f", detalle.getPrecioUnitario()));
                    double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                    table.addCell(String.format("$%.2f", subtotal));
                    table.addCell(detalle.getLote() != null ? String.valueOf(detalle.getLote().getIdLote()) : "N/A");
                    totalVenta += subtotal;
                }

                // Total de la venta
                PdfPCell totalCell = new PdfPCell(new Phrase("Total Venta:"));
                totalCell.setColspan(3);
                totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalCell);

                PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("$%.2f", totalVenta)));
                totalValueCell.setColspan(2);
                table.addCell(totalValueCell);

                document.add(table);
            } else {
                document.add(new Paragraph("No hay detalles para esta venta", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10)));
            }

            // Separador entre ventas
            document.add(new Paragraph(" "));
            document.add(new Paragraph("------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph(" "));
        }

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportClientesToExcel(List<Cliente> clientes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clientes");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID Cliente", "Nombre", "Email", "Teléfono", "Compras"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (Cliente cliente : clientes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cliente.getIdCliente());
                row.createCell(1).setCellValue(cliente.getNombreCliente());
                row.createCell(2).setCellValue(cliente.getCorreo());
                row.createCell(3).setCellValue(cliente.getTelefono());
                // Aquí deberías añadir el número de compras si lo tienes en tu modelo
                row.createCell(4).setCellValue(0); // Reemplaza con el dato real
            }

            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    public ByteArrayInputStream exportClientesToPdf(List<Cliente> clientes) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Título
        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Reporte de Clientes", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Tabla
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Encabezados de tabla
        String[] headers = {"ID Cliente", "Nombre", "Email", "Teléfono", "Dirección"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Datos de tabla
        for (Cliente cliente : clientes) {
            table.addCell(String.valueOf(cliente.getIdCliente()));
            table.addCell(cliente.getNombreCliente());
            table.addCell(cliente.getCorreo());
            table.addCell(cliente.getTelefono());
            table.addCell(cliente.getDireccion());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportProductosToExcel(List<Producto> productos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Productos");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID Producto", "Nombre", "Precio", "Categoria", "Presentacion", "Unidad de medida"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (Producto producto : productos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(producto.getIdProducto());
                row.createCell(1).setCellValue(producto.getNombreProducto());
                row.createCell(3).setCellValue(producto.getPrecio());
                row.createCell(4).setCellValue(producto.getIdCategoria() != null ? producto.getIdCategoria().getDescripcion() : "");
                row.createCell(5).setCellValue(producto.getIdPresentacion() != null ? producto.getIdPresentacion().getDescripcion() : "");
                row.createCell(6).setCellValue(producto.getIdUnidadMedida() != null ? producto.getIdUnidadMedida().getDescripcion() : "");
            }

            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    public ByteArrayInputStream exportProductosToPdf(List<Producto> productos) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Título
        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Reporte de Productos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Tabla
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Encabezados de tabla
        String[] headers = {"ID Producto", "Nombre", "Precio", "Categoría", "Presentación", "Unidad de Medida"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(new java.awt.Color(200, 200, 200));
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Datos de tabla
        for (Producto producto : productos) {
            table.addCell(String.valueOf(producto.getIdProducto()));
            table.addCell(producto.getNombreProducto());
            table.addCell(producto.getPrecio() != null ? producto.getPrecio().toString() : "");
            table.addCell(producto.getIdCategoria() != null ? producto.getIdCategoria().getDescripcion() : "");
            table.addCell(producto.getIdPresentacion() != null ? producto.getIdPresentacion().getDescripcion() : "");
            table.addCell(producto.getIdUnidadMedida() != null ? producto.getIdUnidadMedida().getDescripcion() : "");
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}