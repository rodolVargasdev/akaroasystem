package com.example.akaroa.controller;

import com.example.akaroa.model.Lote;
import com.example.akaroa.model.Producto;
import com.example.akaroa.repository.ProductoRepository;
import com.example.akaroa.repository.ProveedorRepository;
import com.example.akaroa.repository.UsuarioRepository;
import com.example.akaroa.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
//@RequestMapping("api/products")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private LoteController loteController;
    @Autowired
    private LoteService loteService;
    @Autowired
    private PresentacionService presentacionService;
    @Autowired
    private UnidadMedidaService unidadMedidaService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/productos")
    @Transactional
    public String getAllProductos(Model model) {
        model.addAttribute("listarProductos", productoService.findAll());
        model.addAttribute("lotes", loteService.findAll());
        model.addAttribute("lotesIdProduct", idProductoLote());
        model.addAttribute("presentaciones", presentacionService.findAll());
        model.addAttribute("unidadesmedidas", unidadMedidaService.findAll());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("producto", new Producto());
        model.addAttribute("calcularStock", calcularTotalProductos());

        return "productos";
    }

    @GetMapping("/productos/{idProducto}")
    @ResponseBody
    public Producto obtenerProducto(@PathVariable Integer idProducto) {
        if (productoRepository.findById(idProducto).isPresent()) {
            return productoRepository.findById(idProducto).get();
        }
        return null;
    }



    @PostMapping("/productos")
    @Transactional
    public String procesarFormulario(@ModelAttribute Producto producto, Model model) {
        if (producto.getIdCategoria() != null) {
            producto.setIdCategoria(categoriaService.findById(producto.getIdCategoria().getIdCategoria()).orElse(null));
        }
        if (producto.getIdPresentacion() != null) {
            producto.setIdPresentacion(presentacionService.findById(producto.getIdPresentacion().getIdPresentacion()).orElse(null));
        }
        if (producto.getIdUnidadMedida() != null) {
            producto.setIdUnidadMedida(unidadMedidaService.findById(producto.getIdUnidadMedida().getIdUnidadMedida()).orElse(null));
        }

        productoService.save(producto);
        model.addAttribute("agregadoExitoso", "Producto agregado exitosamente"+ producto.getNombreProducto());

        return "redirect:/productos";
    }

    @PostMapping("/productos/editar/{idProducto}")
    public String editarProducto(@PathVariable Integer idProducto, @ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            producto.setIdProducto(idProducto); // Asegúrate de que el ID esté correctamente asignado
            productoService.actualizarProducto(producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto editado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al editar el producto.");
        }
        return "redirect:/productos";
    }


    @PostMapping("/productos/eliminar/{id}")
    @Transactional
    public String eliminarProducto(@PathVariable("id") Integer id, Model model) {
        Optional<Producto> productoOpt = productoService.findById(id);

        System.out.println(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();

            // Obtener los listarLotes asociados al producto
            List<Lote> lotes = loteService.findByProducto(producto);

            // Eliminar los listarLotes asociados
            for (Lote lote : lotes) {
                loteService.delete(lote); // Asegúrate de tener un metodo delete en LoteService
            }

            // Ahora eliminar el producto
            productoService.delete(producto);
            model.addAttribute("mensajeExito", "Producto y listarLotes eliminados correctamente.");
        } else {
            model.addAttribute("mensajeError", "Producto no encontrado.");
        }
        return "redirect:/productos"; // Redirige a la lista de productos
    }

    public List<Integer> idProductoLote(){
        List<Lote> lotes = loteService.findAll();

        List<Integer> idLote = new ArrayList<>();
        for(Lote lote : lotes){

            idLote.add(lote.getIdProducto().getIdProducto());
        }

        return idLote;
    }

    public Integer obtenerCantidadProducto(Producto producto) {
        int cantidadTotal = 0;
        List<Lote> lotes = loteService.findAll();

        for (Lote lote : lotes) {
            if (Objects.equals(lote.getIdProducto().getIdProducto(), producto.getIdProducto())) {
                cantidadTotal += lote.getCantidadProducto();
            }
        }

        return cantidadTotal;
    }

        public List<Integer> calcularTotalProductos() {
            List<Producto> productos = productoService.findAll();
            List<Integer> cantidadesTotales = new ArrayList<>();
            List<Lote> lotes = loteService.findAll();

            for (Producto producto : productos) {
                int cantidadTotal = 0;
                for (Lote lote : lotes) {
                    if (Objects.equals(lote.getIdProducto().getIdProducto(), producto.getIdProducto())) {
                        cantidadTotal += lote.getCantidadProducto();
                    }
                }
                cantidadesTotales.add(cantidadTotal);
            }

            return cantidadesTotales;
        }


}
