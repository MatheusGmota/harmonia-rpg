package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.CadastroRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioRequestDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private static Usuario baseUsuario(String nomeUsuario, String senha) {
        Usuario u = new Usuario();
        u.setNomeUsuario(nomeUsuario);
        u.setSenha(senha);
        return u;
    }

    public static Usuario from(CadastroRequestDTO request) {
        Usuario u = baseUsuario(request.nomeUsuario(), request.senha());
        u.setTipoUsuario(request.tipoUsuario());
        return u;
    }

    public static Usuario from(UsuarioRequestDTO request) {
        return baseUsuario(request.nomeUsuario(), request.senha());
    }
}
