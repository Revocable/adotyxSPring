package br.com.adotyx.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List; // Adicionada a importação

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.adotyx.model.dao.AnimalDAO;
import br.com.adotyx.model.dao.UsuarioDAO;
import net.coobird.thumbnailator.Thumbnails;
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
    public String cadastrar(Animal animal, ModelMap map) {
        // Recuperar o usuário logado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Usuario usuario = udao.findByEmail(email).orElse(null);

        // Definir o tutor (usuário logado) para o animal
        animal.setTutor(usuario);

        map.addAttribute("usuarioLogado", usuario); // Adiciona o usuário logado no modelo
        return "/animal/cadastro"; // Retorna a página de cadastro
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        map.addAttribute("animais", adao.findAll()); // Adiciona lista de animais ao modelo
        return "/animal/listar"; // Retorna a página de listagem
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("animal") Animal animal,
            @RequestParam("foto") MultipartFile foto,
            ModelMap map) {

        // Processar e salvar o arquivo de foto
        if (!foto.isEmpty()) {
            try {
                // Gera um nome único para o arquivo
                String filename = System.currentTimeMillis() + "_" + foto.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/uploads/", filename);
                Files.createDirectories(path.getParent()); // Garante que o diretório exista

                Thumbnails.of(foto.getInputStream())
                        .scale(1)
                        .outputQuality(0.5)
                        .toFile(path.toFile());
                animal.setPathFoto("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                map.addAttribute("message", "Falha ao salvar a foto.");
                return "/animal/cadastro";
            }
        }

        // Recuperar o usuário logado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Usuario usuario = udao.findByEmail(email).orElse(null); // Corrigido para lidar com o Optional

        // Definir o tutor (usuário) para o animal
        animal.setTutor(usuario);

        adao.save(animal); // Salva o animal no banco de dados
        try {
            Thread.sleep(500); // Tempo de espera (meio segundo)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "redirect:/animais/listar"; // Redireciona após salvar
    }

    @GetMapping("/detalhes")
    public String detalhes(@RequestParam("id") Long id, ModelMap map) {
        // Recupera o animal e o usuário logado
        Animal animal = adao.findById(id).orElse(null);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Usuario usuario = udao.findByEmail(email).orElse(null);

        map.addAttribute("animal", animal);
        map.addAttribute("usuarioLogado", usuario); // Passa o usuário logado para o template
        return "/animal/detalhes";
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
        // Recupera o animal a ser excluído
        Animal animal = adao.findById(id).orElse(null);

        // Se o animal não for encontrado, redireciona de volta para a lista
        if (animal == null) {
            return "redirect:/animais/listar";
        }

        // Recupera o usuário logado
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Usuario usuario = udao.findByEmail(email).orElse(null);

        // Verifica se o usuário logado é o tutor do animal
        if (animal.getTutor() != null && animal.getTutor().equals(usuario)) {
            // Se for o tutor, exclui o animal
            adao.deleteById(id);
        }

        // Redireciona para a lista de animais
        return "redirect:/animais/listar";
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
