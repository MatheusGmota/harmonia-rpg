package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.Pontos;
import br.com.api.harmonia_rpg.domain.enums.TipoClasse;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import br.com.api.harmonia_rpg.domain.enums.TipoOrigem;
import br.com.api.harmonia_rpg.domain.enums.TipoTrilha;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;

public class AgenteDTO {
    public record RequestDTO (
            String nome,
            Integer idade,
            String imagemUrl,
            TipoOrigem origem,
            TipoClasse classe,
            TipoTrilha trilha,

            DescricaoDTO descricao,

            Integer agilidade,
            Integer forca,
            Integer intelecto,
            Integer presenca,
            Integer vigor
    ) {}

    public record ResponseDTO (
            @DocumentId
            String id,

            String idUsuario,

            String nome,
            Integer idade,
            String imagemUrl,
            TipoOrigem origem,
            TipoClasse classe,
            TipoTrilha trilha,

            TipoElemento afinidade,
            Integer nivelExposicao,
            Integer esforcoPorRodada,

            Integer agilidade,
            Integer forca,
            Integer intelecto,
            Integer presenca,
            Integer vigor,

            Pontos pontosDeVida,
            Pontos pontosDeEsforco,
            Pontos pontosDeSanidade,

            Integer defesa,
            Integer defesaEsquiva,
            Integer redDanoBloqueando,
            String protecoes,
            String resistencia,
            String deslocamento,

            Timestamp criadoEm
    ) {}
}
