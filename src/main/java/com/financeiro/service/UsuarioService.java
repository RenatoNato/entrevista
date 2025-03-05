package com.financeiro.service;

import com.financeiro.model.Usuario;
import com.financeiro.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean autenticar(String username, String password) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            boolean senhaCorreta = passwordEncoder.matches(password, usuario.getPassword()); // ✅ Comparação correta de senha

            if (senhaCorreta) {
                System.out.println("Login bem-sucedido para o usuário: " + username);
            } else {
                System.out.println("Senha incorreta para o usuário: " + username);
            }

            return senhaCorreta;
        }

        System.out.println("Usuário não encontrado: " + username);
        return false; // ❌ Retorna falso se o usuário não existir
    }


    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRole("ROLE_USER");
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + id + " não encontrado!"));

        usuario.setUsername(usuarioAtualizado.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioAtualizado.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public void excluirUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + id + " não encontrado!"));

        usuarioRepository.delete(usuario);
    }
}
