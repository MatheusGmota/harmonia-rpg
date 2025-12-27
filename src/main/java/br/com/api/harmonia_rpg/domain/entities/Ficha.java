package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.TipoClasse;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import br.com.api.harmonia_rpg.domain.enums.TipoOrigem;
import br.com.api.harmonia_rpg.domain.enums.TipoTrilha;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ficha {

    @DocumentId
    private String id;
    private String idUsuario;

    private String nomeCampanha;

    private String personagem;
    private int idade;
    private TipoOrigem origem;
    private TipoClasse classe;
    private TipoTrilha trilha;
    private TipoElemento afinidade;

    private int nivelExposicao;
    private int esforcoPorRodada;

    private int agilidade;
    private int forca;
    private int intelecto;
    private int presenca;
    private int vigor;

    private Pontos pontosDeVida;
    private Pontos pontosDeEsforco;
    private Pontos pontosDeSanidade;

    private int defesa;
    private int defesaEsquiva;
    private int redDanoBloqueando;
    private String protecoes;
    private String resistencia;
    private String deslocamento;

    @ServerTimestamp
    private Timestamp criadoEm;
}
