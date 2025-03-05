package com.financeiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable() // ✅ Permite acesso ao H2 Console
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll() // ✅ Permite acesso ao H2 Console
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // ✅ Libera o Swagger
                .antMatchers(HttpMethod.POST, "/usuarios/login").permitAll() // ✅ Login sem autenticação
                .antMatchers(HttpMethod.POST, "/usuarios/cadastro").permitAll() // ✅ Cadastro sem autenticação
                .antMatchers(HttpMethod.GET, "/usuarios/listar", "/transferencias/listar").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") // 🔥 Protegendo os endpoints
                .anyRequest().authenticated()
                .and()
                //.csrf().disable()
                //.formLogin().disable() // 🔥 Remove o pop-up de login no navegador
                .httpBasic().disable(); // 🔥 Permite autenticação Basic no Postman

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // ✅ Permite requisições do Angular
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Permite todos os métodos
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // ✅ Permite cabeçalhos necessários
        config.setExposedHeaders(Arrays.asList("Authorization")); // ✅ Permite o navegador receber o Token JWT
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }
}
