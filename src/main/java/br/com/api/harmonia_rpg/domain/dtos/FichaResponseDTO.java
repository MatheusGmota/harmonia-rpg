package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.Pontos;
import br.com.api.harmonia_rpg.domain.enums.TipoClasse;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import br.com.api.harmonia_rpg.domain.enums.TipoOrigem;
import br.com.api.harmonia_rpg.domain.enums.TipoTrilha;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;

public record FichaResponseDTO(
        @DocumentId
        String id,
        String idUsuario,
        String nomeCampanha,
        String imgPersonagem,
        String personagem,
        int idade,
        TipoOrigem origem,
        TipoClasse classe,
        TipoTrilha trilha,
        TipoElemento afinidade,
        int nivelExposicao,
        int esforcoPorRodada,
        int agilidade,
        int forca,
        int intelecto,
        int presenca,
        int vigor,
        Pontos pontosDeVida,
        Pontos pontosDeEsforco,
        Pontos pontosDeSanidade,
        int defesa,
        int defesaEsquiva,
        int redDanoBloqueando,
        String protecoes,
        String resistencia,
        String deslocamento,
        Timestamp criadoEm
) {
}
