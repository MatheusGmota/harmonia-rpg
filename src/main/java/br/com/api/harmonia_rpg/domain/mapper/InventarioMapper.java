package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.InventarioRequestDTO;
import br.com.api.harmonia_rpg.domain.entities.Inventario;

public class InventarioMapper {

    public static Inventario from(InventarioRequestDTO dto) {
        return new Inventario(
                null,
                dto.carga(),
                dto.pontosDePrestigio(),
                dto.patente(),
                dto.limiteCreditos(),
                dto.limiteItens(),
                dto.itens()
                );
    }
}
