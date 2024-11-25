package br.com.adotyx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.adotyx.model.dao.UsuarioDAO;
import br.com.adotyx.domain.Usuario;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioDAO usuarioDAO;

    


    @GetMapping("/login")
    public String login() {
        return "/usuario/login";
    }

    @PostMapping("/salvar")
    public String salvar(Usuario usuario) {
        // Lógica para salvar o usuário, criptografando a senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioDAO.save(usuario);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        map.addAttribute("usuarios", usuarioDAO.findAll());
        return "/usuario/lista";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam("id") Long id, ModelMap map) {
        map.addAttribute("usuario", usuarioDAO.findById(id).orElse(null));
        return "/usuario/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(Usuario usuario) {
        usuarioDAO.save(usuario);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam("id") Long id, ModelMap map) {
        map.addAttribute("usuario", usuarioDAO.findById(id).orElse(null));
        return "/usuario/deletar";
    }

    @PostMapping("/excluir")
    public String excluir(@RequestParam("id") Long id) {
        usuarioDAO.deleteById(id);
        return "redirect:/usuarios/listar";
    }

    @ModelAttribute("usuarios")
    public List<Usuario> getUsuarios() {
        return usuarioDAO.findAll();
    }

    @ModelAttribute("username")
    public String getUsername() {
        String nome = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            nome = userDetails.getUsername();
        }
        return nome;
    }
}
