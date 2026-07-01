package com.tinnova.lojaveiculo.dto.response;

import java.math.BigDecimal;

public record VeiculoResponseDTO(
        Long id,

        String marca,

        String modelo,

        Integer ano,

        String cor,

        String placa,

        BigDecimal precoUsd,

        Boolean ativo
) {
}
