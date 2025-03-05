package com.financeiro.config;

import com.financeiro.model.Usuario;
import com.financeiro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(new BCryptPasswordEncoder().encode("admin")); //  Senha "admin" criptografada
                admin.setRole("ADMIN");
                usuarioRepository.save(admin);
                System.out.println("Usuário admin criado com sucesso!");
            }
        };
    }
}
