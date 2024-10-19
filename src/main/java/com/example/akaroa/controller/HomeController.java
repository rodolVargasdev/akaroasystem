package com.example.akaroa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        return "login";
    }

    @GetMapping("/atributos")
    public String atributos(Model model) {
        return "atributos";
    }

    @GetMapping("/ventas")
    public String ventas(Model model) {
        return "ventas";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/lotes")
    public String lotes(Model model) {
        return "lotes";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        return "productos";
    }

    @GetMapping("/proveedores")
    public String proveedores(Model model) {
        return "proveedores";
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
