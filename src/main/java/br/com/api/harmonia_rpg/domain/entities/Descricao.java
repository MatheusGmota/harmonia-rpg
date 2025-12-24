package br.com.api.harmonia_rpg.domain.entities;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Descricao {
    @DocumentId
    private String idFicha;

    private String aparencia;
    private String personalidade;
    private String historico;
    private String objetivo;
}
