package com.tinnova.lojaveiculo.service.impl;

import com.tinnova.lojaveiculo.service.CambioService;
import com.tinnova.lojaveiculo.service.CotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
@Service
@RequiredArgsConstructor
public class CambioServiceImpl implements CambioService {

    private final CotacaoService cotacaoService;

    @Override
    public BigDecimal converterRealParaDolar(BigDecimal valorEmReais) {

        BigDecimal cotacao = cotacaoService.obterCotacaoDolar();

        return valorEmReais.divide(cotacao, 2, RoundingMode.HALF_UP);
    }
}