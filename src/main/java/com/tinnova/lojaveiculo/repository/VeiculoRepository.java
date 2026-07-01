package com.tinnova.lojaveiculo.repository;

import com.tinnova.lojaveiculo.dto.response.MarcaRelatorioDTO;
import com.tinnova.lojaveiculo.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {

    /**
     * Procura um veículo ativo pelo id.
     */
    Optional<Veiculo> findByIdAndAtivoTrue(Long id);

    /**
     * Verifica se já existe um veículo com a placa informada.
     */
    boolean existsByPlaca(String placa);

    /**
     * Relatório de quantidade de veículos por marca.
     */
    @Query("""
            SELECT new com.tinnova.lojaveiculo.dto.response.MarcaRelatorioDTO(
                    v.marca,
                    COUNT(v)
            )
            FROM Veiculo v
            WHERE v.ativo = true
            GROUP BY v.marca
            ORDER BY COUNT(v) DESC
            """)
    List<MarcaRelatorioDTO> findVeiculoCountByMarca();


}
