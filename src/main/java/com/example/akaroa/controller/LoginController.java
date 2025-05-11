package com.example.akaroa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Pattern;

@Controller
public class LoginController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@akaroa\\.com$");

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model, HttpSession session) {

        // manejo de errores de autenticacion
        if (request.getParameter("error") != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                model.addAttribute("error", "Usuario o contraseña incorrectos!");
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
            return "login";
        }

        // Manejo de logout
        if (request.getParameter("logout") != null) {
            model.addAttribute("logout", "Has cerrado sesión correctamente");
            return "login";
        }

        // Manejo de email inválido
        if (request.getParameter("invalidEmail") != null) {
            model.addAttribute("error", "Dirección de correo no válida");
            return "login";
        }

        return "login";
    }

    @PostMapping("/login")
    public String validateEmail(@RequestParam("username") String email,
                                RedirectAttributes redirectAttributes) {
        if (!isValidEmail(email)) {
            redirectAttributes.addAttribute("invalidEmail", true);
        }
        return "redirect:/login";
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}


