package com.tinnova.lojaveiculo.service;

import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoPatchDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoUpdateDTO;
import com.tinnova.lojaveiculo.dto.response.MarcaRelatorioDTO;
import com.tinnova.lojaveiculo.dto.response.VeiculoResponseDTO;
import com.tinnova.lojaveiculo.entity.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VeiculoService {

    Page<VeiculoResponseDTO> listarVeiculos(
            String brand,
            Integer year,
            String color,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);

    VeiculoResponseDTO detalharVeiculo(Long id);

    VeiculoResponseDTO adicionarVeiculo(VeiculoCreateDTO dto);

    VeiculoResponseDTO atualizarVeiculo(
            Long id,
            VeiculoUpdateDTO dto);

    VeiculoResponseDTO atualizarParcialmenteVeiculo(
            Long id,
            VeiculoPatchDTO dto);

    void removerVeiculo(Long id);

    List<MarcaRelatorioDTO> gerarRelatorioPorMarca();

}
