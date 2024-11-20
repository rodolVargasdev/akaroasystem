package com.example.akaroa.controller;

import com.example.akaroa.model.Usuario;
import com.example.akaroa.model.Rol;
import com.example.akaroa.service.RolService;
import com.example.akaroa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
//import java.util.Optional;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @GetMapping("/registrousuario")
    public String mostrarFormRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        List<Rol> roles = rolService.findAll();
        model.addAttribute("roles", roles);

        return "registrousuario";
    }

    @PostMapping("/registrousuario")
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

        return "registrousuario";
    }

//    @GetMapping("/usuarios")
//    public String mostrarUsuarios(@RequestParam(required = false) String searchTerm, Model model) {
//        List<Usuario> usuarios = usuarioService.buscar(searchTerm);
//        model.addAttribute("usuarios", usuarios);
//        model.addAttribute("searchTerm", searchTerm);
//
//        return  "registrousuario";
//    }

}
