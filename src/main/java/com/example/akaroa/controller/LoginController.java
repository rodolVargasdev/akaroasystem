package com.example.akaroa.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.regex.Pattern;

@Controller
public class LoginController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\\\w.-]+@akaroa\\.com$");


    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "invalidEmail", required = false) String invalidEmail,
                        Model model, HttpSession session) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos!");
        }
        if (logout != null) {
            model.addAttribute("logout", "Has cerrado sesión");
        }
        if (invalidEmail != null) {
            model.addAttribute("error", "Direccion de correo no válida");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String email) {
        if (!isValidEmail(email)) {
            return "redirect:/login?invalidEmail=true";
        }

        return "redirect:/productos";
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}


