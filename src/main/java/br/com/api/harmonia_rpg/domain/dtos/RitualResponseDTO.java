package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.CustoRitual;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;

public record RitualResponseDTO(
        String nomeRitual,
        TipoElemento tipoElemento,
        CustoRitual custoRitual,

        String execucao,
        String alcance,
        String alvo,
        String duracao,
        String resistencia,
        String descricao,
        int dtRitual,

        String danoSanidade,
        int circulo
) {
}
