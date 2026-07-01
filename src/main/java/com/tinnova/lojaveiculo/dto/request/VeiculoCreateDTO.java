package com.tinnova.lojaveiculo.dto.request;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
public record VeiculoCreateDTO(
        @NotBlank(message = "A marca é obrigatória.")
        @Size(max = 50)
        String marca,

        @NotBlank(message = "O modelo é obrigatório.")
        @Size(max = 80)
        String modelo,

        @NotNull(message = "O ano é obrigatório.")
        @Min(value = 1900)
        Integer ano,

        @NotBlank(message = "A cor é obrigatória.")
        @Size(max = 30)
        String cor,

        @NotBlank(message = "A placa é obrigatória.")
        @Pattern(
                regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
                message = "Placa inválida."
        )
        String placa,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.01")
        BigDecimal precoBrl

) {
}
