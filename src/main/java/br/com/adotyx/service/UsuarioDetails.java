    package br.com.adotyx.service;

    import br.com.adotyx.domain.Usuario;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import java.util.List;

    public class UsuarioDetails implements UserDetails {

        private final Usuario usuario;
        private final PasswordEncoder passwordEncoder;

        public UsuarioDetails(Usuario usuario, PasswordEncoder passwordEncoder) {
            this.usuario = usuario;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public List<GrantedAuthority> getAuthorities() {
            // Adicionando o tipo de autoridade do usuário
            return List.of(() -> usuario.getTipo().name());
        }

        @Override
        public String getPassword() {
            return usuario.getSenha();  // Retorna a senha criptografada armazenada no banco de dados
        }

        @Override
        public String getUsername() {
            return usuario.getEmail();  // Retorna o e-mail como o username
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        // Método para verificar se a senha fornecida é válida
        public boolean checkPassword(String rawPassword) {
            return passwordEncoder.matches(rawPassword, this.usuario.getSenha());
        }
    }
