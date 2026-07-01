package com.tinnova.lojaveiculo.service.impl;

import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoPatchDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoUpdateDTO;
import com.tinnova.lojaveiculo.dto.response.MarcaRelatorioDTO;
import com.tinnova.lojaveiculo.dto.response.VeiculoResponseDTO;
import com.tinnova.lojaveiculo.entity.Veiculo;
import com.tinnova.lojaveiculo.exception.PlacaDuplicadaException;
import com.tinnova.lojaveiculo.exception.ResourceNotFoundException;
import com.tinnova.lojaveiculo.repository.VeiculoRepository;
import com.tinnova.lojaveiculo.service.CambioService;
import com.tinnova.lojaveiculo.service.VeiculoService;
import com.tinnova.lojaveiculo.specification.VeiculoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VeiculoServiceImpl implements VeiculoService {

    private final CambioService cambioService;

    private final VeiculoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<VeiculoResponseDTO> listarVeiculos(
            String marca,
            Integer ano,
            String cor,
            BigDecimal minPreco,
            BigDecimal maxPreco,
            Pageable pageable) {

        Specification<Veiculo> specification =
                VeiculoSpecification.withFilters(
                        marca,
                        ano,
                        cor,
                        minPreco,
                        maxPreco);

        return repository.findAll(specification, pageable)
                .map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public VeiculoResponseDTO detalharVeiculo(Long id) {

        Veiculo veiculo = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veículo não encontrado."));

        return toResponseDTO(veiculo);
    }

    @Override
    public VeiculoResponseDTO adicionarVeiculo(
            VeiculoCreateDTO dto) {

        if (repository.existsByPlaca(dto.placa())) {
            throw new PlacaDuplicadaException(dto.placa());
        }

        Veiculo veiculo = new Veiculo();

        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setAno(dto.ano());
        veiculo.setCor(dto.cor());
        veiculo.setPlaca(dto.placa());

        // Conversão BRL -> USD
        veiculo.setPrecoUsd(cambioService.converterRealParaDolar(dto.precoBrl()));

        veiculo.setAtivo(true);

        repository.save(veiculo);

        return toResponseDTO(veiculo);
    }

    @Override
    public VeiculoResponseDTO atualizarVeiculo(
            Long id,
            VeiculoUpdateDTO dto) {

        Veiculo veiculo = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veículo não encontrado."));

        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setAno(dto.ano());
        veiculo.setCor(dto.cor());
        veiculo.setPlaca(dto.placa());

        veiculo.setPrecoUsd(cambioService.converterRealParaDolar(dto.precoBrl()));

        repository.save(veiculo);

        return toResponseDTO(veiculo);
    }

    @Override
    public VeiculoResponseDTO atualizarParcialmenteVeiculo(
            Long id,
            VeiculoPatchDTO dto) {

        Veiculo veiculo = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veículo não encontrado."));

        if (dto.marca() != null)
            veiculo.setMarca(dto.marca());

        if (dto.modelo() != null)
            veiculo.setModelo(dto.modelo());

        if (dto.ano() != null)
            veiculo.setAno(dto.ano());

        if (dto.cor() != null)
            veiculo.setCor(dto.cor());

        if (dto.placa() != null)
            veiculo.setPlaca(dto.placa());

        if (dto.precoBrl() != null)
            veiculo.setPrecoUsd(cambioService.converterRealParaDolar(dto.precoBrl()));

        repository.save(veiculo);

        return toResponseDTO(veiculo);
    }

    @Override
    public void removerVeiculo(Long id) {

        Veiculo veiculo = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veículo não encontrado."));

        veiculo.setAtivo(false);

        repository.save(veiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaRelatorioDTO> gerarRelatorioPorMarca() {

        return repository.findVeiculoCountByMarca();
    }

    /**
     * Mapper Entity -> DTO
     */
    private VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {

        return new VeiculoResponseDTO(

                veiculo.getId(),

                veiculo.getMarca(),

                veiculo.getModelo(),

                veiculo.getAno(),

                veiculo.getCor(),

                veiculo.getPlaca(),

                veiculo.getPrecoUsd(),

                veiculo.getAtivo()

        );
    }

}
