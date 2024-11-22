package com.example.akaroa.controller;

import com.example.akaroa.model.Categoria;
import com.example.akaroa.model.Presentacion;
import com.example.akaroa.model.Producto;
import com.example.akaroa.model.UnidadMedida;
import com.example.akaroa.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
//@RequestMapping("api/lots")
public class AtributosController {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private PresentacionService presentacionService;
    @Autowired
    private UnidadMedidaService unidadMedidaService;
    @Autowired
    private LoteService loteService;
    @Autowired
    private ProductoService productoService;

    @GetMapping("/atributos")
    public String getAllAtributos(Model model) {
        model.addAttribute("listarCategorias", categoriaService.findAll());
        model.addAttribute("listarPresentaciones", presentacionService.findAll());
        model.addAttribute("listarUnidadesMedida", unidadMedidaService.findAll());
        model.addAttribute("unidadMedida", new UnidadMedida());
        model.addAttribute("presentacion", new Presentacion());
        model.addAttribute("categoria", new Categoria());

        return "atributos";
    }


    /* Categoria atributos endpoints */

    @GetMapping("/atributos/categorias/{idCategoria}")
    @ResponseBody
    public Categoria getCategoriaById(@PathVariable int idCategoria) {
        if(categoriaService.findById(idCategoria).isPresent()) {
            return categoriaService.findById(idCategoria).get();
        }
        return null;
    }

    @PostMapping("/atributos/categorias")
    @Transactional
    public String procesarFormularioCategorias(@ModelAttribute Categoria categoria, Model model, RedirectAttributes redirectAttributes) {

        System.out.println(categoria);

        categoriaService.save(categoria);
        redirectAttributes.addFlashAttribute("mensaje", "Categoria creada con exito");

        return "redirect:/atributos";
    }

    @PostMapping("/atributos/categorias/editar/{idCategoria}")
    public String editarCategoria(@PathVariable Integer idCategoria, @ModelAttribute("categoria") Categoria categoria, RedirectAttributes redirectAttributes) {

        System.out.println(idCategoria);

        Optional<Categoria> categoriaExistente = categoriaService.findById(idCategoria);

        if(categoriaExistente.isPresent()) {
            Categoria categoriaActualizada = categoriaExistente.get();
            categoriaActualizada.setDescripcion(categoria.getDescripcion());
            categoriaService.save(categoriaActualizada);
            redirectAttributes.addFlashAttribute("mensaje", "Categoria editada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Categoria no encontrada");
        }
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/categorias/eliminar/{idCategoria}")
    @Transactional
    public String eliminarCategoria(@PathVariable Integer idCategoria, RedirectAttributes redirectAttributes) {

        System.out.println( idCategoria );

        List<Producto> productos = productoService.findAll();

        for(Producto producto: productos) {
            if(producto.getIdPresentacion().getIdPresentacion().equals(idCategoria)) {
                redirectAttributes.addFlashAttribute("error", "El atributo no puede ser eliminado debido a que está siendo utilizado por un producto.");
                return "redirect:/atributos";
            }
        }


        if(categoriaService.findById(idCategoria).isPresent()) {
            categoriaService.deleteById(idCategoria);
            redirectAttributes.addFlashAttribute("mensaje", "Categoria eliminado con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Categoria no encontrada");
        }
        return "redirect:/atributos";
    }


    /* Presentacion atributo endpoints */

    @GetMapping("/atributos/presentaciones/{idPresentacion}")
    @ResponseBody
    public Presentacion getPresentacionById(@PathVariable int idPresentacion) {
        if(presentacionService.findById(idPresentacion).isPresent()) {
            return presentacionService.findById(idPresentacion).get();
        }
        return null;
    }

    @PostMapping("/atributos/presentaciones")
    @Transactional
    public String procesarFormularioPresentaciones(@ModelAttribute Presentacion presentacion, Model model, RedirectAttributes redirectAttributes) {

        System.out.println(presentacion);

        presentacionService.save(presentacion);
        redirectAttributes.addFlashAttribute("mensaje", "Categoria creada con exito");
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/presentaciones/editar/{idPresentacion}")
    public String editarPresentacion(@PathVariable Integer idPresentacion, @ModelAttribute("presentacion") Presentacion presentacion, RedirectAttributes redirectAttributes) {

        System.out.println(idPresentacion);

        Optional <Presentacion> presentacionExistente = presentacionService.findById(idPresentacion);

        if(presentacionExistente.isPresent()) {
            Presentacion presentacionActualizada = presentacionExistente.get();
            presentacionActualizada.setDescripcion(presentacion.getDescripcion());
            presentacionService.save(presentacionActualizada);
            redirectAttributes.addFlashAttribute("mensaje", "Presentacion editada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Presentacion no encontrada");
        }
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/presentaciones/eliminar/{idPresentacion}")
    @Transactional
    public String eliminarPresentacion(@PathVariable Integer idPresentacion, RedirectAttributes redirectAttributes) {

        System.out.println( idPresentacion );

        List<Producto> productos = productoService.findAll();

        for(Producto producto: productos) {
            if(producto.getIdPresentacion().getIdPresentacion().equals(idPresentacion)) {
                redirectAttributes.addFlashAttribute("error", "El atributo no puede ser eliminado debido a que está siendo utilizado por un producto.");
                return "redirect:/atributos";
            }
        }

        if(presentacionService.findById(idPresentacion).isPresent()) {
            presentacionService.deleteById(idPresentacion);
            redirectAttributes.addFlashAttribute("mensaje", "Presentacion eliminada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Presentacion no encontrada");
        }
        return "redirect:/atributos";
    }

    /*Unidades de Medida atributo  */

    @GetMapping("/atributos/unidadesmedida/{idUnidadMedida}")
    @ResponseBody
    public UnidadMedida getUnidadMedidaById(@PathVariable int idUnidadMedida) {
        if(unidadMedidaService.findById(idUnidadMedida).isPresent()) {
            return unidadMedidaService.findById(idUnidadMedida).get();
        }
        return null;
    }

    @PostMapping("/atributos/unidadesmedida")
    @Transactional
    public String procesarFormularioUnidadesMedida(@ModelAttribute UnidadMedida unidadMedida, Model model, RedirectAttributes redirectAttributes) {

        System.out.println(unidadMedida);

        unidadMedidaService.save(unidadMedida);
        redirectAttributes.addFlashAttribute("mensaje", "Unidad de medida creada con exito");
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/unidadesmedida/editar/{idUnidadMedida}")
    public String editarUnidadMedida(@PathVariable Integer idUnidadMedida, @ModelAttribute("unidadMedida") UnidadMedida unidadMedida, RedirectAttributes redirectAttributes) {

        System.out.println(idUnidadMedida);

        Optional <UnidadMedida> unidadMedidaExistente = unidadMedidaService.findById(idUnidadMedida);

        if(unidadMedidaExistente.isPresent()) {
            UnidadMedida unidadMedidaActualizada = unidadMedidaExistente.get();
            unidadMedidaActualizada.setDescripcion(unidadMedida.getDescripcion());
            unidadMedidaService.save(unidadMedidaActualizada);
            redirectAttributes.addFlashAttribute("mensaje", "Unidad de medida editada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unidad de medida no encontrada");
        }
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/unidadesmedida/eliminar/{idUnidadMedida}")
    @Transactional
    public String eliminarUnidadMedida(@PathVariable Integer idUnidadMedida, RedirectAttributes redirectAttributes) {

        System.out.println(idUnidadMedida);

        List<Producto> productos = productoService.findAll();

        for(Producto producto: productos) {
            if(producto.getIdPresentacion().getIdPresentacion().equals(idUnidadMedida)) {
                redirectAttributes.addFlashAttribute("error", "El atributo no puede ser eliminado debido a que está siendo utilizado por un producto.");
                return "redirect:/atributos";
            }
        }

        if(unidadMedidaService.findById(idUnidadMedida).isPresent()) {
            unidadMedidaService.deleteById(idUnidadMedida);
            redirectAttributes.addFlashAttribute("mensaje", "Unidad de medida eliminada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unidad de medida no encontrada");
        }
        return "redirect:/atributos";
    }
}
