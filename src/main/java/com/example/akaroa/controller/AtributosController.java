package com.example.akaroa.controller;

import com.example.akaroa.model.Categoria;
import com.example.akaroa.model.Presentacion;
import com.example.akaroa.model.UnidadMedida;
import com.example.akaroa.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        categoriaService.save(categoria);
        redirectAttributes.addFlashAttribute("mensaje", "Categoria creada con exito");

        return "redirect:/atributos";
    }

    @PostMapping("/atributos/categorias/editar/{idCategoria}")
    public String editarCategoria(@PathVariable Integer idCategoria, Model model, RedirectAttributes redirectAttributes) {

        Optional <Categoria> categoriaExistente = categoriaService.findById(idCategoria);
        if(categoriaExistente.isPresent()) {
            Categoria categoriaActualizada = categoriaExistente.get();
            categoriaActualizada.setDescripcion(categoriaActualizada.getDescripcion());
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
        if(categoriaService.findById(idCategoria).isPresent()) {
            loteService.deleteById(idCategoria);
            redirectAttributes.addFlashAttribute("mensaje", "Categoria eliminado con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Categoria no encontrada");
        }
        return "redirect:/atributos";
    }


    /*Presentacion atributo*/

    @GetMapping("/atributos/categorias/{idPresentacion}")
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
        presentacionService.save(presentacion);
        redirectAttributes.addFlashAttribute("mensaje", "Categoria creada con exito");
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/presentaciones/editar/{idPresentacion}")
    public String editarPresentacion(@PathVariable Integer idPresentacion, Model model, RedirectAttributes redirectAttributes) {

        Optional <Presentacion> presentacionExistente = presentacionService.findById(idPresentacion);
        if(presentacionExistente.isPresent()) {
            Presentacion presentacionActualizada = presentacionExistente.get();
            presentacionActualizada.setDescripcion(presentacionActualizada.getDescripcion());
            presentacionService.save(presentacionActualizada);
            redirectAttributes.addFlashAttribute("mensaje", "Categoria editada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Categoria no encontrada");
        }
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/presentaciones/eliminar/{idPresentacion}")
    @Transactional
    public String eliminarPresentacion(@PathVariable Integer idPresentacion, RedirectAttributes redirectAttributes) {
        if(presentacionService.findById(idPresentacion).isPresent()) {
            presentacionService.deleteById(idPresentacion);
            redirectAttributes.addFlashAttribute("mensaje", "Presentacion eliminada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Presentacion no encontrada");
        }
        return "redirect:/atributos";
    }

    /*Presentacion atributo*/

    @GetMapping("/atributos/categorias/{idUnidadMedida}")
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
        unidadMedidaService.save(unidadMedida);
        redirectAttributes.addFlashAttribute("mensaje", "Unidad de medida creada con exito");
        return "redirect:/atributos";
    }

    @PostMapping("/atributos/unidadesmedida/editar/{idUnidadMedida}")
    public String editarUnidadMedida(@PathVariable Integer idUnidadMedida, Model model, RedirectAttributes redirectAttributes) {

        Optional <UnidadMedida> unidadMedidaExistente = unidadMedidaService.findById(idUnidadMedida);
        if(unidadMedidaExistente.isPresent()) {
            UnidadMedida unidadMedidaActualizada = unidadMedidaExistente.get();
            unidadMedidaActualizada.setDescripcion(unidadMedidaActualizada.getDescripcion());
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
        if(unidadMedidaService.findById(idUnidadMedida).isPresent()) {
            unidadMedidaService.deleteById(idUnidadMedida);
            redirectAttributes.addFlashAttribute("mensaje", "Presentacion eliminada con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Presentacion no encontrada");
        }
        return "redirect:/atributos";
    }
}
