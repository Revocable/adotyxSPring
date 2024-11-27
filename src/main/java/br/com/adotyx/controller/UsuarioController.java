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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.adotyx.model.dao.UsuarioDAO;
import jakarta.transaction.Transactional;
import br.com.adotyx.domain.Tipo;
import br.com.adotyx.domain.Usuario;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.adotyx.model.dao.MensagemDAO;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private MensagemDAO mensagemDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/cadastrar")
    public String cadastro(ModelMap map) {
        map.addAttribute("usuario", new Usuario()); // Adicionando o objeto vazio para o formulário
        return "/usuario/cadastro"; // Nome da página de cadastro
    }

    @GetMapping("/login")
    public String login() {
        return "/usuario/login"; // Nome da página de login
    }

    @GetMapping("/logout")
    public String logout() {
        // Redireciona para a página inicial (ou qualquer outra) após logout
        return "redirect:/";
    }

    @PostMapping("/salvar")
    public String salvar(Usuario usuario) {
        // Lógica para salvar o usuário, criptografando a senha
        usuario.setTipo(Tipo.USER);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioDAO.save(usuario);
        return "redirect:/usuarios/listar"; // Redireciona para a lista de usuários após salvar
    }

    @GetMapping("/listar")
    public String listar(ModelMap map) {
        map.addAttribute("usuarios", usuarioDAO.findAll()); // Lista todos os usuários
        return "/usuario/lista";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam("id") Long id, ModelMap map) {
        map.addAttribute("usuario", usuarioDAO.findById(id).orElse(null)); // Carrega um usuário específico para editar

        return "/usuario/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        // Obtém o usuário logado
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) {
            return "redirect:/usuarios/login"; // Redireciona para o login se não autenticado
        }
    
        // Atualiza apenas os campos permitidos
        usuarioLogado.setNome(usuario.getNome());
        usuarioLogado.setEmail(usuario.getEmail());
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuarioLogado.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
    
        // Salva as alterações no banco
        usuarioDAO.save(usuarioLogado);
        
        // Adiciona uma mensagem de sucesso
        redirectAttributes.addFlashAttribute("success", "Dados atualizados com sucesso!");
    
        return "redirect:/"; 
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam("id") Long id, ModelMap map) {
        map.addAttribute("usuario", usuarioDAO.findById(id).orElse(null)); // Carrega um usuário específico para
                                                                           // confirmar exclusão
        return "/usuario/deletar";
    }

    @PostMapping("/excluir")
    @Transactional
    public String excluir(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // Verifica se o usuário existe antes de tentar excluir
            if (!usuarioDAO.existsById(id)) {
                redirectAttributes.addFlashAttribute("error", "Usuário não encontrado");
                return "redirect:/usuarios/listar";
            }

            // Exclui todas as mensagens onde o usuário é o remetente ou destinatário
            mensagemDAO.deleteByRemetenteId(id);
            mensagemDAO.deleteByDestinatarioId(id);

            // Exclui o usuário
            usuarioDAO.deleteById(id);

            // Adiciona mensagem de sucesso
            redirectAttributes.addFlashAttribute("success", "Usuário excluído com sucesso");

            // Redireciona para a lista de usuários após exclusão
            return "redirect:/usuarios/listar";
        } catch (Exception e) {
            // Log do erro (considere usar um logger como SLF4J)
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir usuário: " + e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }

    @GetMapping("/conta")
    public String conta(ModelMap map) {
        // Obtém o usuário logado
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) {
            return "redirect:/usuarios/login"; // Redireciona para login se não estiver autenticado
        }
        map.addAttribute("usuario", usuarioLogado); // Passa o usuário para o modelo
        return "/usuario/account"; // Nome do template da conta
    }

    // Método para listar todos os usuários
    @ModelAttribute("usuarios")
    public List<Usuario> getUsuarios() {
        return usuarioDAO.findAll();
    }

    @ModelAttribute("usuarioLogado")
    public Usuario getUsuarioLogado() {
        // Pega o principal (usuário logado) da sessão do Spring Security
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            // Busca o usuário no banco de dados usando o email (usuário logado)
            return usuarioDAO.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null; // Caso não haja usuário logado, retorna null
    }
}
