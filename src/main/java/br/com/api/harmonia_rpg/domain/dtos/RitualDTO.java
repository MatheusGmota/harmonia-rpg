package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.CustoRitual;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import jakarta.validation.constraints.NotBlank;

public class RitualDTO {
    public record RitualResponseDTO(
            String idRitual,
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
    ){};

    public record RitualRequestDTO(
            @NotBlank(message = "O nome do ritual não pode ser vazio")
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
    ){};
}
