package com.example.akaroa.controller;

import com.example.akaroa.model.Lote;
import com.example.akaroa.model.Proveedor;
import com.example.akaroa.repository.ProveedorRepository;
import com.example.akaroa.service.LoteService;
import com.example.akaroa.service.ProveedorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/proveedores")
    public String getAllProveedores(Model model) {

        model.addAttribute("listarProveedores", proveedorService.findAll());
        model.addAttribute("proveedor", new Proveedor());

       return "proveedores";
    }

    @GetMapping("/proveedores/{idProveedor}")
    @ResponseBody
    public Proveedor obtenerProveedor(@PathVariable Integer idProveedor) {
        if(proveedorRepository.findById(idProveedor).isPresent()) {
            return proveedorRepository.findById(idProveedor).get();
        }
        return null;
    }

    @PostMapping("/proveedores")
    @Transactional
    public String procesarFormularioProveedores(@ModelAttribute Proveedor proveedor, Model model, RedirectAttributes redirectAttributes) {

        System.out.println(proveedor.getIdProveedor());

        proveedorService.save(proveedor);
        model.addAttribute("agregadoExitoso", "Proveedor agregado exitosamente con identificador: " + proveedor.getIdProveedor());

        return "redirect:/proveedores";
    }

    @PostMapping("/proveedores/editar/{idProveedor}")
    public String editarProveedor(@PathVariable Integer idProveedor, @ModelAttribute("proveedor") Proveedor proveedor, RedirectAttributes redirectAttributes) {

        System.out.println(idProveedor);

        Optional<Proveedor> proveedorExistente = proveedorService.findById(idProveedor);
        if(proveedorExistente.isPresent()) {
            Proveedor proveedorActualizar = proveedorExistente.get();
            proveedorActualizar.setIdProveedor(proveedor.getIdProveedor());
            proveedorActualizar.setNombreProveedor(proveedor.getNombreProveedor());
            proveedorActualizar.setDireccion(proveedor.getDireccion());
            proveedorActualizar.setTelefono(proveedor.getTelefono());
            proveedorService.save(proveedorActualizar);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado con exito");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor no encontrado");
        }
        return  "redirect:/proveedores";
    }

    @PostMapping("/proveedores/eliminar/{idProveedor}")
    @Transactional
    public String eliminarProveedor(@PathVariable Integer idProveedor, RedirectAttributes redirectAttributes) {
        Optional<Proveedor> proveedorExistente = proveedorService.findById(idProveedor);
        if(proveedorExistente.isPresent()) {
            proveedorService.deleteById(idProveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor eliminado con exito");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor no encontrado");
        }

        return "redirect:/proveedores";
    }

}
