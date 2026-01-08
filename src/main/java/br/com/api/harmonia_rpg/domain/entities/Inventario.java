package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.TipoPatente;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
    @DocumentId
    private String idFicha;
    private Carga carga;
    private int pontosDePrestigio;
    private TipoPatente patente;
    private String limiteCreditos;

    private LimiteItens limiteItens;

    private List<Item> itens;

    public void setCargaAtual() {
        try {
            this.carga.setAtual(this.itens.toArray().length);
            if (this.carga.getAtual() > this.carga.getTotal()) {
                throw new IllegalArgumentException("Seus itens excederam sua carga máxima, você terá penalidades ao se movimentar");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
