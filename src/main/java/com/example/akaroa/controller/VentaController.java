package com.example.akaroa.controller;

import com.example.akaroa.dto.VentaForm;
import com.example.akaroa.model.*;
import com.example.akaroa.repository.VentaRepository;
import com.example.akaroa.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class VentaController {

    private final VentaService ventaService;
    private final VentaRepository ventaRepository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final VentaDetalleService ventaDetalleService;
    private final LoteService loteService;

    @Autowired
    public VentaController(VentaService ventaService, VentaRepository ventaRepository,
                           ClienteService clienteService, UsuarioService usuarioService,
                           ProductoService productoService, VentaDetalleService ventaDetalleService,
                           LoteService loteService) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.ventaDetalleService = ventaDetalleService;
        this.loteService = loteService;
    }

    // formulario de registro de venta
    @GetMapping("/registrarventa")
    public String mostrarFormularioVenta(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        // datos generales de la venta
        VentaForm ventaForm = new VentaForm();
        // detalles de la venta
        VentaForm.VentaDetalleForm detalleForm = new VentaForm.VentaDetalleForm();

        // obtener usuario en sesion (vendedor)
        if (userDetails != null) {
            ventaForm.setUsuarioId(userDetails.getIdUsuario());
            System.out.println("Usuario en sesion: " + userDetails.getIdUsuario());
            System.out.println("Usuario en sesion: " + userDetails.getNombreCompleto());
        }

        // buscar los clientes
        List<Cliente> clientes = clienteService.findAll();
        System.out.println("Clientes encontrados: " + clientes.size());

        // buscar productos con stock disponible
        List<Producto> listarProductos = new ArrayList<>();
        List<Producto> todosProductos = productoService.findAll();

        for (Producto producto : todosProductos) {
            List<Lote> lotes = loteService.findByProductoOrderByFechaVencimientoAsc(producto);
            int stockTotal = lotes.stream().mapToInt(Lote::getCantidadProducto).sum();
            if (stockTotal > 0) {
                listarProductos.add(producto);
            }
        }

        // cargar el nombre del usuario
        model.addAttribute("nombreCompleto",
                userDetails != null ? userDetails.getNombreCompleto() : "No se ha identificado el usuario");
        // cargar los clientes al modelo
        model.addAttribute("listarClientes", clientes);
        // cargar los productos al modelo
        model.addAttribute("listarProductos", listarProductos);
        model.addAttribute("ventaDetalleForm", detalleForm);
        model.addAttribute("ventaForm", ventaForm);
        return "registroventa";
    }

    // procesar formulario de registro de venta
    @PostMapping("/registrarventa/guardar")
    @Transactional
    public String guardarVenta(@Valid @ModelAttribute("ventaForm") VentaForm ventaForm,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        System.out.println("Registrando venta");
        if (bindingResult.hasErrors()) {
            System.out.println("Error al registrar venta: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            redirectAttributes.addFlashAttribute("error", "Error al registrar venta: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/registrarventa";
        }
        try {
            // validar que hay productos en la venta
            if (ventaForm.getDetalles() == null || ventaForm.getDetalles().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto a la venta");
                return "redirect:/registrarventa";
            }

            /* Validaciones de los detalles de la venta antes de cualquier operacion de guardado*/
            for (VentaForm.VentaDetalleForm detalleForm : ventaForm.getDetalles()) {
                // Validar producto existente
                Producto producto = productoService.findById(detalleForm.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: ID " + detalleForm.getProductoId()));

                // Validar cantidad positiva
                if (detalleForm.getCantidad() <= 0) {
                    throw new RuntimeException("Cantidad inválida para el producto: " + producto.getNombreProducto());
                }

                // Obtener lotes del producto ordenados por fecha de vencimiento
                List<Lote> lotesDisponibles = loteService.findByProductoOrderByFechaVencimientoAsc(producto);
                // Validar stock total disponible
                int stockTotal = lotesDisponibles.stream()
                        .mapToInt(Lote::getCantidadProducto)
                        .sum();

                if (stockTotal < detalleForm.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto() +
                            ". Disponible: " + stockTotal);
                }
            }

            /* Validaciones para los datos generales de la venta */
            // Obtener y validar la existencia del cliente
            Cliente cliente = clienteService.findById(ventaForm.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // obtencion de id de usuario y asignacion del id
            Integer idUsuario = userDetails.getIdUsuario();
            Usuario usuarioVendedor = new Usuario();
            usuarioVendedor.setIdUsuario(idUsuario);

            // Creacion venta
            Venta venta = new Venta();
            venta.setFechaVenta(ventaForm.getFechaVenta());
            venta.setIdCliente(cliente);
            venta.setIdUsuario(usuarioVendedor);

            // Guardar la venta
            Venta ventaGuardada = ventaService.save(venta);

            /* procesamiento de los detalles de la venta */
            for (VentaForm.VentaDetalleForm detalleForm : ventaForm.getDetalles()) {
                Producto producto = productoService.findById(detalleForm.getProductoId()).get();

                List<Lote> lotesDisponibles = loteService.findByProductoOrderByFechaVencimientoAsc(producto);
                int cantidadRestante = detalleForm.getCantidad();

                for (Lote lote : lotesDisponibles) {
                    if (cantidadRestante <= 0) break;

                    int cantidadEnLote = lote.getCantidadProducto();
                    int cantidadADescontar = Math.min(cantidadEnLote, cantidadRestante);

                    // Actualizar cantidad en lote
                    lote.setCantidadProducto(cantidadEnLote - cantidadADescontar);
                    if (lote.getCantidadProducto() == 0) loteService.save(lote); // actualiza la cantidad a 0

                    // Crear detalle de venta
                    VentaDetalle detalle = new VentaDetalle();
                    detalle.setIdVenta(ventaGuardada);
                    detalle.setIdProducto(producto);
                    detalle.setCantidad(cantidadADescontar);
                    detalle.setPrecioUnitario(detalleForm.getPrecioUnitario());
                    detalle.setLote(lote);

                    ventaDetalleService.save(detalle);

                    cantidadRestante -= cantidadADescontar;
                }
            }

            System.out.println("Venta realizada");
            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente. N° Venta: " + ventaGuardada.getIdVenta());
            return "redirect:/registrarventa";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar venta: " + e.getMessage());
            return "redirect:/registrarventa";
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error inesperado al registrar venta");
            return "redirect:/registrarventa";
        }
    }

    // Mostrar la lista de ventas
    @GetMapping("/listaventas")
    public String mostrarListaDeVentas(Model model) {
        model.addAttribute("listaDeVentas", ventaService.findAll());
        return "detallesventa";
    }

    @GetMapping("/venta/{idVenta}")
    @ResponseBody
    public Venta obtenerVenta(@PathVariable Integer idVenta) {
        if (ventaRepository.findById(idVenta).isPresent()) {
            System.out.println("Venta encontrada: " + idVenta);
            return ventaRepository.findById(idVenta).get();
        }
        return null;
    }

    // ver detalles de venta
    @GetMapping("/venta/{idVenta}/detalles")
    @ResponseBody
    public List<VentaDetalle> mostrarDetallesDeVenta(@PathVariable Integer idVenta) {
        System.out.println("Mostrando detalles de venta: " + idVenta);
        return ventaDetalleService.findByVenta(idVenta);
    }

    @PostMapping("/venta/eliminar/{idVenta}")
    @Transactional
    public String eliminarDetalleVenta(@PathVariable Integer idVenta, RedirectAttributes redirectAttributes) {
        try {
            // busqueda de venta a eliminar por id
            Venta venta = ventaService.findById(idVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));

            // buscar los detalles de la venta
            List<VentaDetalle> detalles = ventaDetalleService.findByVenta(idVenta);

            // procesar cada detalle de venta para la devolucion de stock a los lotes
            for (VentaDetalle detalle : detalles) {
                // encontrar los id de los productos y lotes a devolver
                Producto producto = detalle.getIdProducto();
                Lote lote = detalle.getLote();
                // encontrar la cantidad a devolver
                int cantidadDevuelta = detalle.getCantidad();

                // Verificar si el lote aún existe
                Optional<Lote> loteActualizado = loteService.findById(lote.getIdLote());
                if (loteActualizado.isPresent()) {
                    // Devolver la cantidad al lote
                    Lote loteExistente = loteActualizado.get();
                    loteExistente.setCantidadProducto(loteExistente.getCantidadProducto() + cantidadDevuelta);
                    loteService.save(loteExistente);
                } else {
                    // Si el lote fue eliminado, crear uno nuevo con la cantidad devuelta
                    Lote nuevoLote = new Lote();
                    nuevoLote.setIdProducto(producto);
                    nuevoLote.setCantidadProducto(cantidadDevuelta);
                    nuevoLote.setFechaVencimiento(lote.getFechaVencimiento());
                    loteService.save(nuevoLote);
                }
                // Eliminar el detalle de venta
                ventaDetalleService.deleteById(detalle.getIdDetalle());
            }

            // eliminar la venta
            ventaService.deleteById(idVenta);
            redirectAttributes.addFlashAttribute("success", "Venta eliminada exitosamente. El stock ha sido devuelto a los lotes.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar venta: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado al eliminar venta: " + e.getMessage());
        }
        return "redirect:/listaventas";
    }

    @PostMapping("/venta/editar/{idVenta}")
    @Transactional
    public String editarDetallesVenta(@PathVariable Integer idVenta,
                                      @RequestParam Map<String, String> formParams,
                                      RedirectAttributes redirectAttributes) {
        try {
            // buscar la venta a editar
            System.out.println("Parámetros recibidos: " + formParams);
            Venta venta = ventaService.findById(idVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            // Procesar cada detalle modificado
            for (String key : formParams.keySet()) {
                if (key.startsWith("detalle_")) {
                    String[] parts = key.split("_");
                    Integer idDetalle = Integer.parseInt(parts[1]);
                    String action = parts[2];

                    VentaDetalle detalle = ventaDetalleService.findById(idDetalle)
                            .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

                    Producto producto = detalle.getIdProducto();
                    Lote loteOriginal = detalle.getLote();
                    int cantidadOriginal = detalle.getCantidad();

                    if (action.equals("cantidad")) {
                        int nuevaCantidad = Integer.parseInt(formParams.get(key));

                        if (nuevaCantidad > cantidadOriginal) {
                            // Aumento de cantidad
                            int diferencia = nuevaCantidad - cantidadOriginal;

                            // Verificar stock disponible
                            List<Lote> lotes = loteService.findByProductoOrderByFechaVencimientoAsc(producto);
                            int stockDisponible = lotes.stream().mapToInt(Lote::getCantidadProducto).sum();

                            if (stockDisponible < diferencia) {
                                throw new RuntimeException("Stock insuficiente para aumentar la cantidad");
                            }

                            // Actualizar lote (usando el mismo lote original si tiene stock)
                            if (loteOriginal.getCantidadProducto() >= diferencia) {
                                loteOriginal.setCantidadProducto(loteOriginal.getCantidadProducto() - diferencia);
                                loteService.save(loteOriginal);
                            } else {
                                // Distribuir la diferencia entre lotes disponibles
                                int restante = diferencia;
                                for (Lote lote : lotes) {
                                    if (restante <= 0) break;

                                    int disponible = lote.getCantidadProducto();
                                    int aDescontar = Math.min(disponible, restante);

                                    lote.setCantidadProducto(disponible - aDescontar);
                                    loteService.save(lote);
                                    restante -= aDescontar;
                                }
                            }

                            detalle.setCantidad(nuevaCantidad);

                        } else if (nuevaCantidad < cantidadOriginal) {
                            // Disminución de cantidad
                            int diferencia = cantidadOriginal - nuevaCantidad;

                            // Devolver al lote original si existe
                            Optional<Lote> loteOpt = loteService.findById(loteOriginal.getIdLote());
                            if (loteOpt.isPresent()) {
                                Lote lote = loteOpt.get();
                                lote.setCantidadProducto(lote.getCantidadProducto() + diferencia);
                                loteService.save(lote);
                            } else {
                                // Si el lote original no existe, crear uno nuevo
                                Lote nuevoLote = new Lote();
                                nuevoLote.setIdProducto(producto);
                                nuevoLote.setCantidadProducto(diferencia);
                                nuevoLote.setFechaVencimiento(loteOriginal.getFechaVencimiento());
                                loteService.save(nuevoLote);
                            }

                            detalle.setCantidad(nuevaCantidad);
                        }

                        ventaDetalleService.save(detalle);

                    } else if (action.equals("eliminar")) {
                        // Eliminar completamente el detalle y devolver to do el stock

                        // Devolver al lote original si existe
                        Optional<Lote> loteOpt = loteService.findById(loteOriginal.getIdLote());
                        if (loteOpt.isPresent()) {
                            Lote lote = loteOpt.get();
                            lote.setCantidadProducto(lote.getCantidadProducto() + cantidadOriginal);
                            loteService.save(lote);
                        } else {
                            // Si el lote original no existe, crear uno nuevo
                            Lote nuevoLote = new Lote();
                            nuevoLote.setIdProducto(producto);
                            nuevoLote.setCantidadProducto(cantidadOriginal);
                            nuevoLote.setFechaVencimiento(loteOriginal.getFechaVencimiento());
                            loteService.save(nuevoLote);
                        }

                        ventaDetalleService.deleteById(idDetalle);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("success", "Venta actualizada exitosamente");
            return "redirect:/listaventas";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al editar venta: " + e.getMessage());
            return "redirect:/listaventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado al editar venta");
            return "redirect:/listaventas";
        }
    }
}
