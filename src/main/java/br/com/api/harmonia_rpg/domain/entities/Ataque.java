package br.com.api.harmonia_rpg.domain.entities;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ataque {
    @DocumentId
    private String idFicha;

    private String nome;
    private String teste;
    private String dano;

    private int critico;
    private String alcance;
    private String especial;
}
