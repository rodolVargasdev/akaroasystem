package com.example.akaroa.controller;

import com.example.akaroa.model.Lote;
import com.example.akaroa.repository.LoteRepository;
import com.example.akaroa.service.LoteService;
import com.example.akaroa.service.ProductoService;
import com.example.akaroa.service.ProveedorService;
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
public class LoteController {

    @Autowired
    private LoteService loteService;
    @Autowired
    private LoteRepository loteRepository;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoService productoService;

    @GetMapping("/lotes")
    public String getAllLotes(Model model) {
        model.addAttribute("listarLotes", loteService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("lote", new Lote());
        return "lotes";
    }



    @GetMapping("/lotes/{idLote}")
    @ResponseBody
    public Lote obtenerLote(@PathVariable Integer idLote) {
        if(loteRepository.findById(idLote).isPresent()) {
            return loteRepository.findById(idLote).get();
        }
        return null;
    }

    @PostMapping("/lotes")
    @Transactional
    public String procesarFormularioLotes(@ModelAttribute Lote lote, Model model, RedirectAttributes redirectAttributes) {


        System.out.println(lote.getIdProducto());

        loteService.save(lote);
        model.addAttribute("agregadoExitoso", "Lote agregado exitosamente con identificador: #"+ lote.getIdLote());

        return "redirect:/lotes";

    }

    @PostMapping("/lotes/editar/{idLote}")
    public String editarLote(@PathVariable Integer idLote, @ModelAttribute("lote") Lote lote, RedirectAttributes redirectAttributes) {

        System.out.println(idLote);

        Optional<Lote> loteExistente = loteService.findById(idLote);
        if (loteExistente.isPresent()) {
            Lote loteActualizado = loteExistente.get();
            loteActualizado.setCantidadProducto(lote.getCantidadProducto());
            loteActualizado.setFechaRecepcion(lote.getFechaRecepcion());
            loteActualizado.setFechaVencimiento(lote.getFechaVencimiento());
            loteActualizado.setIdProveedor(lote.getIdProveedor());
            loteActualizado.setIdProducto(lote.getIdProducto());
            loteService.save(loteActualizado);
            redirectAttributes.addFlashAttribute("mensaje", "Lote actualizado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "El lote no existe.");
        }
        return "redirect:/lotes";
    }

    @PostMapping("/lotes/eliminar/{idLote}")
    @Transactional
    public String eliminarLote(@PathVariable Integer idLote, RedirectAttributes redirectAttributes) {
        if (loteService.findById(idLote).isPresent()) {
            loteService.deleteById(idLote);
            redirectAttributes.addFlashAttribute("mensaje", "Lote eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "El lote no existe.");
        }
        return "redirect:/lotes";
    }


}
