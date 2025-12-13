package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
        UserDetails usuario = repository.buscarPorNomeUsuario(nomeUsuario);
        if (usuario == null) throw new UsernameNotFoundException("Usuário não encontrado");
        return usuario;
    }
}
