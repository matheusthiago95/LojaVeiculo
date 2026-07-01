package com.tinnova.lojaveiculo.service;

import com.tinnova.lojaveiculo.dto.response.FrankfurterResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
@Service
public class CotacaoService {

    private final RestTemplate restTemplate;
    private final String url;

    public CotacaoService(
            RestTemplate restTemplate,
            @Value("${frankfurter.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public BigDecimal obterCotacaoDolar() {

        System.out.println("Consultando API Frankfurter...");

        FrankfurterResponseDTO response =
                restTemplate.getForObject(url, FrankfurterResponseDTO.class);

        return response.rates().get("BRL");
    }
}