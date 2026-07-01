package com.tinnova.lojaveiculo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;

public record VeiculoUpdateDTO(
        @NotBlank(message = "A marca é obrigatória.")
        @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres.")
        String marca,

        @NotBlank(message = "O modelo é obrigatório.")
        @Size(max = 80, message = "O modelo deve ter no máximo 80 caracteres.")
        String modelo,

        @NotNull(message = "O ano é obrigatório.")
        @Min(value = 1900, message = "Ano inválido.")
        Integer ano,

        @NotBlank(message = "A cor é obrigatória.")
        @Size(max = 30, message = "A cor deve ter no máximo 30 caracteres.")
        String cor,

        @NotBlank(message = "A placa é obrigatória.")
        @Pattern(
                regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
                message = "Placa inválida."
        )
        String placa,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        BigDecimal precoBrl

) {
}
