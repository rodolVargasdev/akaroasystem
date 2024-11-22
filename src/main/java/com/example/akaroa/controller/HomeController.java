package com.example.akaroa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "login";
    }

    @GetMapping("/ventas")
    public String ventas(Model model) {
        return "ventas";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "login";
    }

    @GetMapping("/registrousuario")
    public String register(Model model) {
        return "registrousuario";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        return "reportes";
    }

    @GetMapping("/usuario")
    public String usuarios(Model model) {
        return "usuario";
    }
}
