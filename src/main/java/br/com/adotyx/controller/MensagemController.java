package br.com.adotyx.controller;

import br.com.adotyx.domain.Mensagem;
import br.com.adotyx.domain.Usuario;
import br.com.adotyx.model.dao.MensagemDAO;
import br.com.adotyx.model.dao.UsuarioDAO;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mensagem")
public class MensagemController {
    private static final Logger logger = LoggerFactory.getLogger(MensagemController.class);

    @Autowired
    private MensagemDAO mensagemDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private HttpSession session;

    // Método para acessar o chat padrão com a lista de contatos, sem selecionar nenhum contato
    @GetMapping("/chat")
    public String abrirChatPadrao(Model model) {
        logger.info("Acessando chat padrão");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Usuário não logado");
            model.addAttribute("erro", "Usuário não logado");
            return "chat/chat";
        }

        User usuarioLogado = (User) authentication.getPrincipal();
        logger.info("Usuário logado: {}", usuarioLogado.getUsername());

        try {
            String usuarioEmail = usuarioLogado.getUsername();
            Usuario usuarioLogadoEntity = usuarioDAO.findByEmail(usuarioEmail)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            Long usuarioId = usuarioLogadoEntity.getId();

            List<Usuario> contatos = usuarioDAO.findContatosByUsuario(usuarioId);
            if (contatos == null)
                contatos = new ArrayList<>();

            model.addAttribute("usuarioLogado", usuarioLogadoEntity); // Ajustado para exibir o nome real
            model.addAttribute("contatos", contatos);

            // Não há contato selecionado inicialmente
            model.addAttribute("contatoSelecionado", null);
            model.addAttribute("mensagens", new ArrayList<Mensagem>());

            return "chat/chat";
        } catch (Exception e) {
            logger.error("Erro ao carregar chat", e);
            model.addAttribute("erro", "Erro ao carregar chat: " + e.getMessage());
            return "chat/chat";
        }
    }

    @GetMapping("/chat/{usuarioId}")
    public String abrirChatComUsuario(@PathVariable Long usuarioId, Model model) {
        logger.info("Acessando chat com usuário ID: {}", usuarioId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Usuário não logado");
            model.addAttribute("erro", "Usuário não logado");
            return "chat/chat";
        }

        User usuarioLogado = (User) authentication.getPrincipal();

        try {
            String usuarioEmail = usuarioLogado.getUsername();
            Usuario usuarioLogadoEntity = usuarioDAO.findByEmail(usuarioEmail)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            Long usuarioIdLogado = usuarioLogadoEntity.getId();

            List<Usuario> contatos = usuarioDAO.findContatosByUsuario(usuarioIdLogado);
            Usuario contato = usuarioDAO.findById(usuarioId)
                    .orElseThrow(() -> {
                        model.addAttribute("erro", "Usuário não encontrado");
                        model.addAttribute("contatos", contatos);
                        return new RuntimeException("Usuário não encontrado");
                    });

            logger.info("Contato selecionado: {}", contato.getNome());

            List<Mensagem> mensagens = mensagemDAO.findByRemetenteAndDestinatario(usuarioLogadoEntity, contato);

            model.addAttribute("usuarioLogado", usuarioLogadoEntity);
            model.addAttribute("contatoSelecionado", contato);
            model.addAttribute("mensagens", mensagens);
            model.addAttribute("contatos", contatos);

            return "chat/chat";
        } catch (Exception e) {
            logger.error("Erro ao carregar chat com usuário", e);
            model.addAttribute("erro", "Erro ao carregar chat: " + e.getMessage());
            return "chat/chat";
        }
    }

    // Método para enviar mensagens
    @PostMapping("/chat/{usuarioId}/enviar")
    public String enviarMensagem(@PathVariable Long usuarioId,
            @RequestParam("mensagemTexto") String mensagemTexto,
            Model model) {
        logger.info("Enviando mensagem para usuário ID: {}", usuarioId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Usuário não logado");
            model.addAttribute("erro", "Usuário não logado");
            return "chat/chat";
        }

        User usuarioLogado = (User) authentication.getPrincipal();

        try {
            String usuarioEmail = usuarioLogado.getUsername();
            Usuario usuarioLogadoEntity = usuarioDAO.findByEmail(usuarioEmail)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            Usuario destinatario = usuarioDAO.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));

            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(usuarioLogadoEntity);
            mensagem.setDestinatario(destinatario);
            mensagem.setMensagem(mensagemTexto);
            mensagem.setDataEnvio(LocalDateTime.now());

            mensagemDAO.save(mensagem);

            logger.info("Mensagem enviada com sucesso");

            return "redirect:/mensagem/chat/" + usuarioId;
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem", e);
            model.addAttribute("erro", "Erro ao enviar mensagem: " + e.getMessage());
            return "chat/chat";
        }
    }
}
