package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.entities.Pericia;

import java.util.List;

public record AtributoResponseDTO(
        List<Pericia> agilidade,
        List<Pericia> forca,
        List<Pericia> intelecto,
        List<Pericia> presenca,
        List<Pericia> vigor
) {}
