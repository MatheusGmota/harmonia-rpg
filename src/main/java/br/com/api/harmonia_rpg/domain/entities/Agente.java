package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.TipoClasse;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import br.com.api.harmonia_rpg.domain.enums.TipoOrigem;
import br.com.api.harmonia_rpg.domain.enums.TipoTrilha;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.*;

import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agente {

    @DocumentId
    private String id;

    private String idUsuario;

    private String nome;
    private Integer idade;
    private String imagemUrl;
    private TipoOrigem origem;
    private TipoClasse classe;
    private TipoTrilha trilha;

    private TipoElemento afinidade;
    private Integer nivelExposicao;
    private Integer esforcoPorRodada;

    private Integer agilidade;
    private Integer forca;
    private Integer intelecto;
    private Integer presenca;
    private Integer vigor;

    private Pontos pontosDeVida;
    private Pontos pontosDeEsforco;
    private Pontos pontosDeSanidade;

    private Integer defesa;
    private Integer defesaEsquiva;
    private Integer redDanoBloqueando;
    private String protecoes;
    private String resistencia;
    private String deslocamento;

    @ServerTimestamp
    private Timestamp criadoEm;

    public void inicializarClasse() {
        setNivelExposicao(this.nivelExposicao);
        setDefesa(this.defesa);
        setDefesaEsquiva(this.defesaEsquiva);
        setPontosDeVida(this.pontosDeVida);
        setPontosDeEsforco(this.pontosDeEsforco);
        setPontosDeSanidade(this.pontosDeSanidade);
        setEsforcoPorRodada(this.esforcoPorRodada);
        setDeslocamento(this.deslocamento);
        setRedDanoBloqueando(0);
    }

    public void setNivelExposicao(Integer nivelExposicao) {
        if (nivelExposicao == null && this.origem.equals(TipoOrigem.MUNDANO)) this.nivelExposicao = 1;
        else this.nivelExposicao = 5;
    }
    public void setDeslocamento(String deslocamento) {
        this.deslocamento = "9m/6q";
    }

    public void setDefesaEsquiva(Integer defesaEsquiva) {
        if (defesaEsquiva == null) this.defesaEsquiva = this.defesa;
        else this.defesaEsquiva = defesaEsquiva;
    }

    public void setDefesa(Integer defesa) {
        if (defesa == null) this.defesa = this.agilidade + 10;
        else this.defesa = defesa;
    }

    public void setPontosDeSanidade(Pontos pontosDeSanidade) {
        int statusInicial;

        if (this.classe == TipoClasse.COMBATENTE) statusInicial = 12;
        else if (this.classe == TipoClasse.ESPECIALISTA) statusInicial = 16;
        else if (this.classe == TipoClasse.OCULTISTA) statusInicial = 20;
        else {
            statusInicial = 8;
        }

        this.pontosDeSanidade = Objects.requireNonNullElseGet(pontosDeSanidade, () -> new Pontos(statusInicial, statusInicial));
    }

    public void setPontosDeEsforco(Pontos pontosDeEsforco) {
        int statusInicial = this.presenca;

        if (this.classe == TipoClasse.COMBATENTE) statusInicial += 2;
        else if (this.classe == TipoClasse.ESPECIALISTA) statusInicial += 3;
        else if (this.classe == TipoClasse.OCULTISTA) statusInicial += 4;
        else {
            statusInicial = 2;
        }

        int finalStatusInicial = statusInicial;
        this.pontosDeEsforco = Objects.requireNonNullElseGet(pontosDeEsforco, () -> new Pontos(finalStatusInicial, finalStatusInicial));
    }

    public void setPontosDeVida(Pontos pontosDeVida) {
        int statusInicial = this.vigor;

        if (this.classe == TipoClasse.COMBATENTE) statusInicial += 20;
        else if (this.classe == TipoClasse.ESPECIALISTA) statusInicial += 16;
        else if (this.classe == TipoClasse.OCULTISTA) statusInicial += 12;
        else {
            statusInicial = 9;
        }

        int finalStatusInicial = statusInicial;
        this.pontosDeVida = Objects.requireNonNullElseGet(pontosDeVida, () -> new Pontos(finalStatusInicial, finalStatusInicial));
    }

    public void setEsforcoPorRodada(Integer esforcoPorRodada) {
        this.esforcoPorRodada = Objects.requireNonNullElseGet(esforcoPorRodada, () -> {
            if (this.nivelExposicao == null || this.nivelExposicao == 0) return 1;
            return this.nivelExposicao / 5;
        });
    }
}
