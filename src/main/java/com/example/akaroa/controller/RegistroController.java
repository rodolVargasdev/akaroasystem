package com.example.akaroa.controller;

import com.example.akaroa.model.Usuario;
import com.example.akaroa.model.Rol;
import com.example.akaroa.repository.UsuarioRepository;
import com.example.akaroa.service.RolService;
import com.example.akaroa.service.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
//import java.util.Optional;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/registrousuario")
    public String mostrarFormRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        List<Rol> roles = rolService.findAll();
//        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("listarUsuarios", usuarioService.findAll());
        model.addAttribute("roles", roles);
//        model.addAttribute("usuarios", usuarios);

        return "registrousuario";
    }

    @PostMapping("/registrousuario/guardar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

        // Validacion de las contrasenas
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas deben ser iguales");
            model.addAttribute("roles", rolService.findAll());
            return "registrousuario";
        }

        // Verificacion de email en uso
        if (usuarioService.existePorEmail(usuario.getEmail())) {
            System.out.println(usuario.getEmail());
            model.addAttribute("error", "El email ya esta en uso, Por favor ingresa un email diferente");
            model.addAttribute("roles", rolService.findAll());
            return "registrousuario";
        }

        Integer idRol = usuario.getIdRol().getIdRol();
        Rol rol = rolService.obtenerRolPorId(idRol);
//        System.out.println(usuario.getNombreUsuario());

        if(rol==null) {
            System.out.println("Rol no encontrado");
        }

        usuario.setIdRol(rol);
        usuarioService.save(usuario);

        model.addAttribute("success", "Usuario registrado exitosamente");
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("listarUsuarios", usuarioService.findAll());

        return "registrousuario";
    }

    @PostMapping("/registrousuario/eliminar/{idUsuario}")
    @Transactional
    public String eliminarUsuario(@PathVariable Integer idUsuario, RedirectAttributes redirectAttributes) {
        Optional<Usuario> optUsuario = usuarioService.findById(idUsuario);

        if (optUsuario.isPresent()) {
            usuarioService.deleteById(idUsuario);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
        }
        return "redirect:/registrousuario";

    }

    @GetMapping("/registrousuario/{idUsuario}")
    @ResponseBody
    public Usuario obtenerUsuario(@PathVariable Integer idUsuario) {
        if (usuarioRepository.findById(idUsuario).isPresent()) {
            return usuarioRepository.findById(idUsuario).get();
        }
        return null;
    }

    @PostMapping("/registrousuario/editar/{idUsuario}")
    public String editarUsuario(@PathVariable Integer idUsuario, @ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        Optional<Usuario> existeUsuario = usuarioService.findById(idUsuario);

        System.out.println(idUsuario);

        if (existeUsuario.isPresent()) {
            Usuario actualizarUsuario = existeUsuario.get();
            actualizarUsuario.setNombreUsuario(usuario.getNombreUsuario());
            actualizarUsuario.setEmail(usuario.getEmail());

            // Actualiza la contraseña si se ha proporcionado una nueva
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()){
                actualizarUsuario.setPassword(usuario.getPassword());
            }

            Integer idRol = usuario.getIdRol().getIdRol();
            Rol rol = rolService.obtenerRolPorId(idRol);

            if(rol != null) {
                actualizarUsuario.setIdRol(rol);
            } else {
                redirectAttributes.addFlashAttribute("error", "El rol no encontrado");
                return "redirect:/registrousuario";
            }

            usuarioService.save(actualizarUsuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado con exito");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
        }
        return "redirect:/registrousuario";
    }
}
