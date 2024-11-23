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
//                    http.requestMatchers(HttpMethod.POST, "/productos/editar/**")
//                            .hasAnyRole("ADMIN", "SELLER", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/productos/eliminar/**")
//                            .hasAnyRole("ADMIN", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/lotes/editar/**")
//                            .hasAnyRole("ADMIN", "SELLER", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/lotes/eliminar/**")
//                            .hasAnyRole("ADMIN", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/atributos/categorias/editar/**")
//                            .hasAnyRole("ADMIN", "SELLER", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/atributos/categorias/eliminar/**")
//                            .hasAnyRole("ADMIN", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/proveedores/editar/**")
//                            .hasAnyRole("ADMIN", "MANAGER");
//                    http.requestMatchers(HttpMethod.POST, "/proveedores/eliminar/**")
//                            .hasAnyRole("ADMIN", "MANAGER");
                    http.requestMatchers(HttpMethod.POST, "/productos/editar/**",
                                    "/lotes/editar/**",
                                    "/atributos/categorias/editar/**",
                                    "/atributos/presentaciones/editar/**",
                                    "/atributos/unidadesmedida/editar/**"
                            )
                            .hasAnyRole("ADMIN", "SELLER", "MANAGER");

                    http.requestMatchers(HttpMethod.POST, "/productos/eliminar/**",
                                    "/lotes/eliminar/**",
                                    "/atributos/categorias/eliminar/**",
                                    "/atributos/presentaciones/eliminar/**",
                                    "/atributos/unidadesmedida/eliminar/**",
                                    "/proveedores/editar/**",
                                    "/proveedores/eliminar/**")
                            .hasAnyRole("ADMIN", "MANAGER");
                    http.requestMatchers(HttpMethod.POST, "/registrousuario/eliminar/**",
                                    "/registrousuario/editar/**", "/registrousuario/guardar")
                            .hasRole("ADMIN");
                })
                .authorizeHttpRequests((auth) -> auth
                                // Rutas publicas sin autenticacion
                                .requestMatchers("/", "/login"
                                        , "/css/**", "/images/**", "/fonts/**", "/favicon.ico")
                                .permitAll()
                                .requestMatchers("/productos", "/ventas")
                                .hasAnyRole("ADMIN", "MANAGER", "SELLER")
//                        .requestMatchers(HttpMethod.POST, "productos/**").hasRole("ADMIN")
                                .requestMatchers("/lotes", "/proveedores", "/reportes")
                                .hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/roles", "/usuario", "/registrousuario")
                                .hasRole("ADMIN")
                                .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login") // Pagina de login de @Akaroa
                        .loginProcessingUrl("/login") // URL para el procesamiento de inicio de sesion
                        // ruta por defecto despues del login exitoso
                        .defaultSuccessUrl("/productos")
                        .failureUrl("/login?error=true") // redireccion en caso de un error en el login
                        .permitAll()
                )
                .logout(logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutUrl("/login")
//                                .logoutSuccessUrl("/logout.done") // redirige al login cuando cierra la sesion
                                .logoutSuccessUrl("/login?logout=true") // redirige al login cuando cierra la sesion
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                                .permitAll()
                )
                .exceptionHandling(ex -> ex
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.sendRedirect(request.getContextPath() + "/accessDenied");
                                })
//                .exceptionHandling(ex -> ex //redirige al login cuando se intenta acceder a una ruta sin los permisos
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            response.getWriter().write("No tienes acceso a " + request.getRequestURI());
//                        })
                );
        return httpSecurity.build();
    }


    // Configuracion de encriptacion
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
