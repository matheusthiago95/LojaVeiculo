package com.tinnova.lojaveiculo.serviceTest;

import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoUpdateDTO;
import com.tinnova.lojaveiculo.dto.response.VeiculoResponseDTO;
import com.tinnova.lojaveiculo.entity.Veiculo;
import com.tinnova.lojaveiculo.exception.BusinessException;
import com.tinnova.lojaveiculo.exception.PlacaDuplicadaException;
import com.tinnova.lojaveiculo.exception.ResourceNotFoundException;
import com.tinnova.lojaveiculo.repository.VeiculoRepository;
import com.tinnova.lojaveiculo.service.CambioService;
import com.tinnova.lojaveiculo.service.impl.VeiculoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceImplTest {

    @Mock
    private VeiculoRepository repository;

    @Mock
    private CambioService cambioService;

    @InjectMocks
    private VeiculoServiceImpl service;

    // teste de duplicidade de placa
    @Test
    void deveFalharQuandoPlacaDuplicada() {
        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "Honda", "Civic", 2020, "Preto", "ABC1D23", BigDecimal.TEN
        );

        when(repository.existsByPlaca("ABC1D23")).thenReturn(true);

        assertThrows(PlacaDuplicadaException.class,
                () -> service.adicionarVeiculo(dto));
    }

    // teste do cadastro de veiculo
    @Test
    void deveAdicionarVeiculoComSucesso() {
        VeiculoCreateDTO dto = new VeiculoCreateDTO(
                "Honda", "Civic", 2020, "Preto", "ABC1D23", BigDecimal.TEN
        );

        when(cambioService.converterRealParaDolar(dto.precoBrl()))
                .thenReturn(BigDecimal.valueOf(20000));
        when(repository.existsByPlaca("ABC1D23")).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        VeiculoResponseDTO response = service.adicionarVeiculo(dto);

        assertNotNull(response);
        assertEquals("Honda", response.marca());
    }

    // teste de atualização para veiculo inexistente
    @Test
    void deveFalharAoAtualizarVeiculoInexistente() {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

        VeiculoUpdateDTO dto = new VeiculoUpdateDTO(
                "Honda", "Civic", 2020, "Preto", "ABC1D23", BigDecimal.TEN
        );

        assertThrows(ResourceNotFoundException.class,
                () -> service.atualizarVeiculo(1L, dto));
    }

    // teste do delte de veiculo
    @Test
    void deveSoftDelete() {
        Veiculo v = new Veiculo();
        v.setAtivo(true);

        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(v));

        service.removerVeiculo(1L);

        assertFalse(v.getAtivo());
        verify(repository).save(v);
    }
}
