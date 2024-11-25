package br.com.adotyx.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String principal(Model model) {
        // Recupera o nome do usuário logado, se houver
        String username = null;
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = user.getUsername(); // Pega o nome de usuário
        } catch (Exception e) {
            // Caso o usuário não esteja logado, o username será nulo
        }

        // Passa o nome do usuário para o modelo
        model.addAttribute("username", username);
        return "home"; // O nome da página home.html
    }
}
