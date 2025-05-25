package com.example.akaroa.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final String nombreCompleto;
    private final String rol;
    private final Integer idUsuario;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CustomUserDetails(String username, String password, String nombreCompleto, String rol, Integer idUsuario,
                             Collection<? extends GrantedAuthority> authorities) {
        this(username, password, nombreCompleto, rol, idUsuario, authorities, true, true, true, true);
    }

    public CustomUserDetails(String username, String password, String nombreCompleto, String rol, Integer idUsuario,
                             Collection<? extends GrantedAuthority> authorities,
                             boolean accountNonExpired, boolean accountNonLocked,
                             boolean credentialsNonExpired, boolean enabled) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.idUsuario = idUsuario;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }


    // Implementación de los métodos requeridos por UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Métodos adicionales para acceder a los datos personalizados
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Integer getIdUsuario() {return idUsuario;}

    public String getRolTraducido() {
        return switch (rol) {
            case "ADMIN" -> "Administrador";
            case "MANAGER" -> "Gerente";
            case "SELLER" -> "Vendedor";
            default -> rol;
        };
    }
}
