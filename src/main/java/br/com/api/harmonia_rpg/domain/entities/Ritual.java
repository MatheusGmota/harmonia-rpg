package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.TipoElemento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ritual {
    private String nomeRitual;
    private TipoElemento tipoElemento;
    private CustoRitual custoRitual;

    private String execucao;
    private String alcance;
    private String alvo;
    private String duracao;
    private String resistencia;
    private String descricao;
    private int dtRitual;

    private String danoSanidade = "Fazer um teste de ocultismo (DT = 20+qtd de PE gastos). Se falhar, toma o dano de sanidade igual ao número de PE gastos para fazer o ritual.";
    private int circulo;
}
