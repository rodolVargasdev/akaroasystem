package com.example.akaroa.controller;

import com.example.akaroa.model.Rol;
import com.example.akaroa.model.Usuario;
import com.example.akaroa.repository.UsuarioRepository;
import com.example.akaroa.service.RolService;
import com.example.akaroa.service.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        model.addAttribute("listarUsuarios", usuarioService.findAll());
        model.addAttribute("roles", roles);

        return "registrousuario";
    }

    @PostMapping("/registrousuario/guardar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes, Model model) {
        // Lista para acumular errores
        List<String> errores = new ArrayList<>();

        // validar direccion de email
        String emailPattern = "^[A-Za-z0-9._%+-]+@akaroa\\.com$";
        if (!usuario.getEmail().matches(emailPattern)) {
            errores.add("El correo debe tener el formato nombreUsuario@akaroa.com");
        }

        // Verificación de email en uso
        if (usuarioService.existePorEmail(usuario.getEmail())) {
            errores.add("El email ya está en uso, Por favor ingresa un email diferente");
        }

        // Verificacion de nombre de usuario en uso
        if (usuarioService.existePorNombreUsuario(usuario.getNombreUsuario())) {
            errores.add("Este nombre de usuario ya esta en uso, Por favor ingresa un nombre de usuario diferente");
        }

        // Validación de contraseñas
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            errores.add("Las contraseñas deben ser iguales");
        }

        Integer idRol = usuario.getIdRol().getIdRol();
        Rol rol = rolService.obtenerRolPorId(idRol);

        if (rol == null) {
            errores.add("Rol no encontrado");
        }

        if (!errores.isEmpty()) {
            if (errores.size() == 1) {
                errores.add(0, "Por favor, corrige el siguiente error antes de registrar el usuario:");
            } else {
                errores.add(0, "Por favor, resuelve los siguientes errores antes de registrar el usuario:");
            }
            model.addAttribute("errores", errores);
            model.addAttribute("roles", rolService.findAll());
            model.addAttribute("listarUsuarios", usuarioService.findAll());
            return "registrousuario";
        }

        usuario.setIdRol(rol);
        usuarioService.save(usuario);

        // Usar RedirectAttributes para pasar el mensaje de éxito
        redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
        return "redirect:/registrousuario";
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
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                actualizarUsuario.setPassword(usuario.getPassword());
            }

            Integer idRol = usuario.getIdRol().getIdRol();
            Rol rol = rolService.obtenerRolPorId(idRol);

            if (rol != null) {
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
