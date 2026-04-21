package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.service.interfaces.AgenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public abstract class GenericService {
    @Autowired
    private AgenteService agenteService;

    protected void verificaAcessoFicha(String idUsuario, String idFicha) throws ExecutionException, InterruptedException{
        AgenteDTO.AgenteResponseDTO ficha = agenteService.obter(idUsuario, idFicha); // verifica se ficha existe

        // verificar se o usuário é dono da ficha
        if (!ficha.idUsuario().equals(idUsuario)) {
            log.warn("Usuário id={} tentou acessar ficha id={} sem ter permissão", idUsuario, ficha.id());
            throw new BusinessException("Sem permissão para acessar ficha");
        }
    }
}
