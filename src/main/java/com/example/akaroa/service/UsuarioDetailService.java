package com.example.akaroa.service;

import com.example.akaroa.model.Usuario;
import com.example.akaroa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//import java.util.regex.Pattern;

@Service
public class UsuarioDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

//    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\\\w.-]+@akaroa\\.com$");

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busqueda de usuario por email en la bd
        System.out.println(email);
//        if (!EMAIL_PATTERN.matcher(email).matches()) {
//            throw new UsernameNotFoundException("Formato de correo inválido");
//        }
//
//        return usuarioRepository.findByEmail(email)
//                .map(usuario -> User.builder()
//                        .username(usuario.getEmail())
//                        .password(usuario.getPassword())
//                        .roles(usuario.getIdRol().getNombreRole())
//                        .build())
//
//                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        System.out.println(usuario.getNombreUsuario());
        System.out.println(usuario.getEmail());
        System.out.println(usuario.getIdRol().getNombreRole());
        // Creacion de objeto UserDetails
        // User.builder carga detalles de un usuario desde la bd en este caso
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getIdRol().getNombreRole())
                .build();
    }

}

//@Service
//public class UsuarioDetailService implements UserDetailsService {
//
//    @Autowired
//    private UsuarioRepository usuarioRepositorio;
//
//    @Override
//    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
//        Optional<Usuario> usuario = usuarioRepositorio.findByUsername(nombreUsuario);
//        if(usuario.isPresent()) {
//            var userObj = usuario.get();
//            return User.builder()
//                    .username(userObj.getNombreUsuario())
//                    .password(userObj.getPassword())
//                    .build();
//        } else {
//            throw new UsernameNotFoundException(nombreUsuario);
//        }
//    }
//}