package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.Ficha;
import com.google.cloud.Timestamp;

public record FichaUsuarioDTO(String id, String imgPersonagem, String personagem, String nomeCampanha,Timestamp criadoEm) {

    public static FichaUsuarioDTO from(Ficha ficha) {
        return new FichaUsuarioDTO(ficha.getId(), ficha.getImgPersonagem(), ficha.getPersonagem(), ficha.getNomeCampanha(), ficha.getCriadoEm());
    }
}
