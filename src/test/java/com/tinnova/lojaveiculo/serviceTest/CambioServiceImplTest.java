package com.tinnova.lojaveiculo.serviceTest;


import com.tinnova.lojaveiculo.service.CotacaoService;
import com.tinnova.lojaveiculo.service.impl.CambioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CambioServiceImplTest {

    @Mock
    private CotacaoService cotacaoService;

    private CambioServiceImpl cambioService;

    @BeforeEach
    void setup() {
        cambioService = new CambioServiceImpl(cotacaoService);
    }

    // teste para converter real para dolar
    @Test
    void converterRealParaDolar() {

        when(cotacaoService.obterCotacaoDolar())
                .thenReturn(BigDecimal.valueOf(5.00));

        BigDecimal resultado =
                cambioService.converterRealParaDolar(
                        BigDecimal.valueOf(100));

        assertEquals(
                new BigDecimal("20.00"),
                resultado
        );
    }
}
