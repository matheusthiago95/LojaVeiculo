package com.tinnova.lojaveiculo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VeiculoPatchDTO (
        @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres.")
        String marca,

        @Size(max = 80, message = "O modelo deve ter no máximo 80 caracteres.")
        String modelo,

        @Min(value = 1900, message = "Ano inválido.")
        Integer ano,

        @Size(max = 30, message = "A cor deve ter no máximo 30 caracteres.")
        String cor,

        @Pattern(
                regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
                message = "Placa inválida."
        )
        String placa,

        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        BigDecimal precoBrl
)
{
}
