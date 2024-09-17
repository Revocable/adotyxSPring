package br.com.adotyx.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.adotyx.model.dao.UsuarioDAO;
import br.com.adotyx.domain.Usuario;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("")
    public String index() {
        return "/usuario/index";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Usuario usuario) {
        return "/usuario/cadastro";
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        List<Usuario> usuarios = usuarioDAO.findAll();
        map.addAttribute("usuarios", usuarios);
        return "/usuario/lista";
    }

    @PostMapping("/salvar")
    public String salvar(Usuario usuario) {
        usuarioDAO.save(usuario);
        return "redirect:/usuarios/listar";
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

    @GetMapping("/buscar/nome")
    public String pesquisarPorNome(@RequestParam(name = "nome") String nome, ModelMap map) {
        List<Usuario> usuarios = usuarioDAO.findByNome(nome);
        map.addAttribute("usuarios", usuarios);
        return "/usuario/lista";
    }

    @ModelAttribute("usuarios")
    public List<Usuario> getUsuarios() {
        return usuarioDAO.findAll();
    }
}
