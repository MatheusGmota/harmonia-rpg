package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.dtos.CadastroRequestDTO;
import br.com.api.harmonia_rpg.domain.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails  {

    @DocumentId
    private String id;

    private String nomeUsuario;

    private String senha;

    private TipoUsuario tipoUsuario;

    @ServerTimestamp
    private Timestamp criadoEm;

    @Exclude
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipoUsuario == TipoUsuario.MESTRE) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Exclude
    @Override
    public String getUsername() {
        return nomeUsuario;
    }

    @Exclude
    @Override
    public String getPassword() {
        return senha;
    }

    @Exclude
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Exclude
    @Override
    public boolean isAccountNonLocked() { return true; }

    @Exclude
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Exclude
    @Override
    public boolean isEnabled() { return true; }
}
