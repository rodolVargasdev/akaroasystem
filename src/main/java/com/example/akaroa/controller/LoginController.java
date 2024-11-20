package com.example.akaroa.controller;

//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
public class LoginController {

//    private AuthenticationManager authenticationManager;

//    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\\\w.-]+@akaroa\\.com$");

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "invalidEmail", required = false) String invalidEmail,
                        Model model) {
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
//        return email != null && email.contains("@") && email.endsWith(".com");
//        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
//        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
        return EMAIL_PATTERN.matcher(email).matches();
    }


//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password, Model model) {
//        if (!isValidEmail(username)) {
//            model.addAttribute("error", "Usuario o contraseña incorrectos!");
//            return "login";
//        }
//        try {
//            Authentication auth = authenticationManager.
//                    authenticate(new UsernamePasswordAuthenticationToken(username, password)
//                    );
//            SecurityContextHolder.getContext().setAuthentication(auth);
//            return "redirect:/productos";
//        } catch (BadCredentialsException e) {
//            model.addAttribute("error", "Usuario o contrasela incorrectos!");
//            System.out.println("Homelo chino"); // ultimate debuggin test
//            return "login";
//        }
//
//    }


}


