package com.tinnova.lojaveiculo.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record FrankfurterResponseDTO(

        BigDecimal amount,

        String base,

        String date,

        Map<String, BigDecimal> rates

) {
}
