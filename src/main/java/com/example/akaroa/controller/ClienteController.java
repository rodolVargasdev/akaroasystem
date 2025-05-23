package com.example.akaroa.controller;

import com.example.akaroa.model.Cliente;
import com.example.akaroa.repository.ClienteRepository;
import com.example.akaroa.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/clientes")
    public String mostrarClientes(@RequestParam(required = false) String nombreCliente, Model model) {
        List<Cliente> listaClientes;

        if (nombreCliente != null && !nombreCliente.isEmpty()) {
            listaClientes = clienteRepository.findByNombreClienteContainingIgnoreCase(nombreCliente);
            System.out.println("Clientes encontrados: " + listaClientes.size());
        } else {
            listaClientes = clienteService.findAll();
            System.out.println("Clientes encontrados: " + listaClientes.size());
        }

        model.addAttribute("listarClientes", listaClientes);
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("terminoBusqueda", nombreCliente);
        return "clientes";
    }

    @PostMapping("/clientes/registrar")
    @Transactional
    public String registrarCliente(@ModelAttribute Cliente cliente,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        List<String> errores = new ArrayList<>();
        // Validar formato de correo electrónico
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!cliente.getCorreo().matches(emailRegex)) {
            errores.add("El formato del correo electrónico no es válido");
        }

        // Validar que el dominio del correo sea válido
        String email = cliente.getCorreo().toLowerCase();
        if (!email.endsWith("@gmail.com") && !email.endsWith("@outlook.com") &&
                !email.endsWith("@hotmail.com") && !email.endsWith("@yahoo.com")) {
            errores.add("Por favor utilice un proveedor de correo válido (Gmail, Outlook, Hotmail, Yahoo)");
        }

        // Validar correo no repetido
        if (clienteRepository.existsByCorreo(cliente.getCorreo())) {
            errores.add("Ya existe un cliente registrado con este correo electrónico");
        }

        // Validar nombre de cliente no repetido
        if (clienteRepository.existsByNombreCliente(cliente.getNombreCliente())) {
            errores.add("Ya existe un cliente registrado con este nombre");
        }

        if (!errores.isEmpty()) {
            if (errores.size() == 1) {
                errores.add(0, "Por favor, corrige el siguiente error antes de registrar el usuario:");
            } else {
                errores.add(0, "Por favor, resuelve los siguientes errores antes de registrar el usuario:");
            }
            model.addAttribute("errores", errores);
            model.addAttribute("listarClientes", clienteService.findAll());
            return "clientes";
        }

        // Si todas las validaciones pasan, guardar el cliente
        clienteService.save(cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente registrado exitosamente");
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{idCliente}")
    @ResponseBody
    public Cliente obtenerCliente(@PathVariable Integer idCliente) {
        if (clienteRepository.findById(idCliente).isPresent()) {
            return clienteRepository.findById(idCliente).get();
        }
        return null;
    }

    @PostMapping("/clientes/editar/{idCliente}")
    public String editarCliente(@PathVariable Integer idCliente,
                                @ModelAttribute("cliente") Cliente cliente,
                                RedirectAttributes redirectAttributes) {
        Optional<Cliente> clienteExistente = clienteService.findById(idCliente);
        System.out.println(idCliente);

        if (clienteExistente.isPresent()) {
            Cliente clienteActualizar = clienteExistente.get();
            clienteActualizar.setNombreCliente(cliente.getNombreCliente());
            clienteActualizar.setIdCliente(cliente.getIdCliente());
            clienteActualizar.setDireccion(cliente.getDireccion());
            clienteActualizar.setTelefono(cliente.getTelefono());
            clienteService.save(clienteActualizar);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado con exito");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
        }
        return  "redirect:/clientes";
    }

    @PostMapping("/clientes/eliminar/{idCliente}")
    @Transactional
    public String eliminarCliente(@PathVariable Integer idCliente, RedirectAttributes redirectAttributes) {
        Optional<Cliente> clienteExistente = clienteService.findById(idCliente);

        if(clienteExistente.isPresent()) {
            clienteService.deleteById(idCliente);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor eliminado con exito");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor no encontrado");
        }

        return "redirect:/clientes";
    }
}
