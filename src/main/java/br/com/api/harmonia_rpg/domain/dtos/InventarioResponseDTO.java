package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.Carga;
import br.com.api.harmonia_rpg.domain.entities.Item;
import br.com.api.harmonia_rpg.domain.entities.LimiteItens;
import br.com.api.harmonia_rpg.domain.enums.TipoPatente;

import java.util.List;

public record InventarioResponseDTO(
        Carga carga,
        int pontosDePrestigio,
        TipoPatente patente,
        String limiteCreditos,

        LimiteItens limiteItens,

        List<Item>itens
) {
}
