package com.tinnova.lojaveiculo.mapper;

import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoUpdateDTO;
import com.tinnova.lojaveiculo.dto.response.VeiculoResponseDTO;
import com.tinnova.lojaveiculo.entity.Veiculo;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public Veiculo toEntity(VeiculoCreateDTO dto) {

        Veiculo veiculo = new Veiculo();

        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setAno(dto.ano());
        veiculo.setCor(dto.cor());
        veiculo.setPlaca(dto.placa());
        veiculo.setPrecoUsd(dto.precoBrl());

        return veiculo;
    }

    public VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {

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

    public void updateEntity(
            VeiculoUpdateDTO dto,
            Veiculo veiculo) {

        veiculo.setMarca(dto.marca());
        veiculo.setModelo(dto.modelo());
        veiculo.setAno(dto.ano());
        veiculo.setCor(dto.cor());
        veiculo.setPlaca(dto.placa());
        veiculo.setPrecoUsd(dto.precoBrl());

    }
}
