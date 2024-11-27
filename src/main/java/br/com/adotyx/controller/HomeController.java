package br.com.adotyx.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.com.adotyx.domain.Usuario;
import br.com.adotyx.model.dao.UsuarioDAO;

@Controller
public class HomeController {

    private final UsuarioDAO usuarioDAO;

    public HomeController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/")
    public String principal(Model model) {
        // Recupera o nome e o ID do usuário logado, se houver
        String username = null;
        Long usuarioId = null;

        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = user.getUsername(); // Pega o nome de usuário

            // Recupera o usuário logado através do e-mail (username)
            Usuario usuarioLogadoEntity = usuarioDAO.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            usuarioId = usuarioLogadoEntity.getId(); // Recupera o ID do usuário logado
        } catch (Exception e) {
            // Caso o usuário não esteja logado, o username e o id serão nulos
        }

        model.addAttribute("username", username);
        model.addAttribute("usuarioId", usuarioId); // Adiciona o ID do usuário ao modelo
        return "home"; // O nome da página home.html
    }
}
