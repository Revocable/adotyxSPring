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
import br.com.adotyx.model.dao.AnimalDAO;
import br.com.adotyx.model.dao.UsuarioDAO;
import br.com.adotyx.domain.Animal;
import br.com.adotyx.domain.Usuario;

@Controller
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalDAO adao;

    @Autowired
    private UsuarioDAO udao;

    @GetMapping("")
    public String index() {
        return "/animal/index";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Animal animal) {
        return "/animal/cadastro"; // Retorna a página de cadastro
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        map.addAttribute("animais", adao.findAll()); // Adiciona lista de animais ao modelo
        return "/animal/listar"; // Retorna a página de listagem
    }

    @PostMapping("/salvar")
    public String salvar(Animal animal) {
        adao.save(animal); // Salva o animal no banco de dados
        return "redirect:/animais/listar"; // Redireciona após salvar
    }

    @GetMapping("/editar")
    public String editar(@RequestParam("id") Long id, ModelMap map) {
        map.addAttribute("animal", adao.findById(id).orElse(null));
        return "/animal/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(Animal animal) {
        adao.save(animal); // Atualiza o animal no banco de dados
        return "redirect:/animais/listar"; // Redireciona após atualizar
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam("id") Long id, ModelMap map) {
        map.addAttribute("animal", adao.findById(id).orElse(null));
        return "/animal/deletar";
    }

    @PostMapping("/excluir")
    public String excluir(@RequestParam("id") Long id) {
        adao.deleteById(id); // Exclui o animal do banco de dados
        return "redirect:/animais/listar"; // Redireciona após excluir
    }

    @ModelAttribute("tutores")
    public List<Usuario> getTutores() {
        return udao.findAll(); // Retorna a lista de tutores (usuários) para o formulário
    }

    @GetMapping("/buscar/nome")
    public String pesquisarPorNome(@RequestParam(name = "nome") String nome, ModelMap map) {
        map.addAttribute("animais", adao.findByNome(nome)); // Busca animais pelo nome
        return "/animal/listar"; // Retorna a página de listagem
    }

    @GetMapping("/buscar/raca")
    public String pesquisarPorRaca(@RequestParam(name = "raca") String raca, ModelMap map) {
        map.addAttribute("animais", adao.findByRaca(raca)); // Busca animais pela raça
        return "/animal/listar"; // Retorna a página de listagem
    }
}
