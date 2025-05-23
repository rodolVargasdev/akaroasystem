package com.example.akaroa.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.GET,
                                    "/registrarventa",
                                    "/clientes")
                            .hasAnyRole("ADMIN", "MANAGER", "SELLER");
                    http.requestMatchers(HttpMethod.POST,
                                    "/productos/editar/**",
                                    "/lotes/editar/**",
                                    "/atributos/categorias/editar/**",
                                    "/atributos/presentaciones/editar/**",
                                    "/atributos/unidadesmedida/editar/**",
                                    "/registrarventa",
                                    "/clientes/registrar"
                            )
                            .hasAnyRole("ADMIN", "SELLER", "MANAGER");

                    http.requestMatchers(HttpMethod.POST,
                                    "/productos/eliminar/**",
                                    "/lotes/eliminar/**",
                                    "/atributos/categorias/eliminar/**",
                                    "/atributos/presentaciones/eliminar/**",
                                    "/atributos/unidadesmedida/eliminar/**",
                                    "/proveedores/editar/**",
                                    "/proveedores/eliminar/**",
                                    "clientes/editar/**",
                                    "clientes/eliminar/**")
                            .hasAnyRole("ADMIN", "MANAGER");
                    http.requestMatchers(HttpMethod.POST,
                                    "/registrousuario/eliminar/**",
                                    "/registrousuario/editar/**",
                                    "/registrousuario/guardar")
                            .hasRole("ADMIN");
                })
                .authorizeHttpRequests((auth) -> auth
                        // Rutas publicas sin autenticacion
                        .requestMatchers("/", "/login"
                                , "/css/**", "/img/**", "/fonts/**", "/favicon.ico")
                        .permitAll()
                        .requestMatchers("/productos", "/registrarventa")
                        .hasAnyRole("ADMIN", "MANAGER", "SELLER")
                        .requestMatchers("/lotes", "/proveedores", "/reportes")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/roles", "/usuario", "/registrousuario")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login") // Pagina de login de @Akaroa
                        .loginProcessingUrl("/login") // URL para el procesamiento de inicio de sesion
                        .usernameParameter("email")
                        // ruta por defecto despues del login exitoso
                        .defaultSuccessUrl("/productos", true)
                        .failureUrl("/login?error") // redireccion en caso de un error en el login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout") // redirige al login cuando cierra la sesion
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException)
                                -> response.sendRedirect(request.getContextPath() + "/accessDenied"))
                );
        return httpSecurity.build();
    }

    // Configuracion de encriptacion
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
