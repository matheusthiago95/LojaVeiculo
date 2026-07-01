package com.tinnova.lojaveiculo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

        @NotBlank(message = "Usuário é obrigatório.")
        String login,

        @NotBlank(message = "Senha é obrigatória.")
        String senha

) {
}