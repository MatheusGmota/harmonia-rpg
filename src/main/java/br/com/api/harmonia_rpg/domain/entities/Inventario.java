package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.TipoPatente;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public void setItens(List<Item> itens) {
        this.itens = (itens != null) ? itens : new ArrayList<>();
        atualizarCargaAtual();
    }

    // Método de negócio para adicionar item
    public void adicionarItem(Item item) {
        if (this.itens == null) this.itens = new ArrayList<>();
        this.itens.add(item);
        atualizarCargaAtual();
    }

    // Lógica centralizada de cálculo por espaços
    private void atualizarCargaAtual() {
        if (this.carga != null) {
            int totalEspacos = this.itens.stream()
                    .mapToInt(Item::getEspacos)
                    .sum();

            this.carga.setAtual(totalEspacos);

            // Opcional: Log ou flag de sobrecarga
            if (this.carga.getAtual() > this.carga.getTotal()) {
                // Aqui você pode definir uma lógica de penalidade
            }
        }
    }

    public void removerItem(String nomeItem) {
        this.itens.removeIf(item -> item.getNomeItem().equalsIgnoreCase(nomeItem));
        atualizarCargaAtual();
    }

    public void editarItem(String nomeItemAntigo, Item itemAtualizado) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getNomeItem().equalsIgnoreCase(nomeItemAntigo)) {
                itens.set(i, itemAtualizado);
                break;
            }
        }
        atualizarCargaAtual();
    }
}
