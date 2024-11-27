package br.com.adotyx.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.adotyx.domain.Usuario;
import br.com.adotyx.model.dao.UsuarioDAO;

import java.util.List;
import java.util.Optional;

import br.com.adotyx.domain.Tipo;

@Service
public class UsuarioService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UsuarioDAO usuarioDAO;
    
    @PostConstruct
    @Transactional
    public void init() {
        try {
            if (usuarioDAO.findByEmail("teste@email.com").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail("teste@email.com");
                usuario.setSenha(passwordEncoder.encode("senha123"));
                usuario.setNome("Usuário Teste");
                usuario.setTipo(Tipo.ADMIN);
                usuarioDAO.save(usuario);
            }
        } catch (Exception e) {
            // Log the specific error
            System.err.println("Error in user initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Transactional
    public void salvarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioDAO.save(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioDAO.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }
    
    @Transactional
    public void atualizarUsuario(Usuario usuario) {
        // Criptografa a senha se ela for alterada
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        usuarioDAO.save(usuario);
    }
    
    @Transactional
    public void deletarUsuario(Long id) {
        usuarioDAO.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorEmail(String email) {
        return usuarioDAO.findByEmail(email).isPresent();
    }
    
    @Transactional
    public Usuario criarNovoUsuario(Usuario usuario) {
        if (existeUsuarioPorEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }
        return salvarNovoUsuario(usuario);
    }
    
    private Usuario salvarNovoUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioDAO.save(usuario);
    }
}