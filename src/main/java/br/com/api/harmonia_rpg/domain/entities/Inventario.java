package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.TipoPatente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
    private Carga carga;
    private int pontosDePrestigio;
    private TipoPatente patente;
    private String limiteCreditos;

    private LimiteItens limiteItens;

    private Iterat<Item> itens;
}
