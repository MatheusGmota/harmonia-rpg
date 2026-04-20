package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.enums.StatusCampanha;
import com.google.cloud.Timestamp;
import jakarta.validation.constraints.NotBlank;


import java.util.List;

public class CampanhaDTO {

    public record RequestDTO(
            @NotBlank String nome,
            String descricao,
            String imagemCapaUrl
    ) {}

    public record ResponseDTO(
            String id,
            String nome,
            String descricao,
            String imagemCapaUrl,
            String idMestre,
            List<String> idJogadores,
            List<String> idAgentes,
            StatusCampanha status,
            String papel,
            Timestamp criadoEm,
            Timestamp atualizadoEm
    ) {}

    public record AgenteNaCampanhaDTO(
            String idFicha,
            String imagemUrl,
            String nomeAgente,
            String nomeCampanha,
            Timestamp criadoEm
    ) {}
}
