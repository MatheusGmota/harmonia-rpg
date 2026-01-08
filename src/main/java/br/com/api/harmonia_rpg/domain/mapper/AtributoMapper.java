package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.AtributoResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Atributos;

public class AtributoMapper {

    public static AtributoResponseDTO toDto(Atributos atributos) {
        return new AtributoResponseDTO(
                atributos.getAgilidade(),
                atributos.getForca(),
                atributos.getIntelecto(),
                atributos.getPresenca(),
                atributos.getVigor()
                );
    }
}
