package com.tinnova.lojaveiculo.constraintTest;

import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoCreateDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    //Aceita os dados validos
    @Test
    void deveAceitarDtoValido() {

        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "Honda",
                "Civic",
                2024,
                "Preto",
                "ABC1D23",
                BigDecimal.valueOf(100000)
        );

        Set<ConstraintViolation<VeiculoCreateDTO>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());

    }

    //Teste para valiar o campo marca vazio
    @Test
    void deveFalharQuandoMarcaEstiverVazia() {

        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "",
                "Civic",
                2024,
                "Preto",
                "ABC1D23",
                BigDecimal.valueOf(100000)
        );

        Set<ConstraintViolation<VeiculoCreateDTO>> violations =
                validator.validate(dto);

        assertEquals(1, violations.size());

        assertEquals(
                "A marca é obrigatória.",
                violations.iterator().next().getMessage());
    }

    //Teste do ano que for menor que 1900
    @Test
    void deveFalharQuandoAnoForMenorQue1900() {

        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "Honda",
                "Civic",
                1800,
                "Preto",
                "ABC1D23",
                BigDecimal.valueOf(100000)
        );

        Set<ConstraintViolation<VeiculoCreateDTO>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    //Teste para valida a placa invalida
    @Test
    void deveFalharQuandoPlacaForInvalida() {

        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "Honda",
                "Civic",
                2024,
                "Preto",
                "1234567",
                BigDecimal.valueOf(100000)
        );

        Set<ConstraintViolation<VeiculoCreateDTO>> violations =
                validator.validate(dto);

        assertEquals(
                "Placa inválida.",
                violations.iterator().next().getMessage());
    }

    // Valida quando o preço for menor ou igual a zero
    @Test
    void deveFalharQuandoPrecoForMenorOuIgualAZero() {

        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "Honda",
                "Civic",
                2024,
                "Preto",
                "ABC1D23",
                BigDecimal.ZERO
        );

        Set<ConstraintViolation<VeiculoCreateDTO>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

}