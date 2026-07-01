package com.tinnova.lojaveiculo.repositoryTest;

import com.tinnova.lojaveiculo.dto.response.MarcaRelatorioDTO;
import com.tinnova.lojaveiculo.entity.Veiculo;
import com.tinnova.lojaveiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class VeiculoRepositoryTest {

    @Autowired
    private VeiculoRepository repository;

    // verifica a existencia de placa
    @Test
    void deveVerificarExistenciaDePlaca() {
        Veiculo v = new Veiculo();
        v.setMarca("Honda");
        v.setModelo("Civic");
        v.setAno(2020);
        v.setCor("Preto");
        v.setPlaca("ABC1D23");
        v.setPrecoUsd(BigDecimal.valueOf(10000));
        v.setAtivo(true);

        repository.save(v);

        boolean exists = repository.existsByPlaca("ABC1D23");

        assertTrue(exists);
    }

    //busca o veiculo pelo id
    @Test
    void deveBuscarVeiculoAtivoPorId() {
        Veiculo v = new Veiculo();
        v.setMarca("Toyota");
        v.setModelo("Etios");
        v.setAno(2015);
        v.setCor("Branco");
        v.setPlaca("TTZ9A23");
        v.setPrecoUsd(BigDecimal.valueOf(12000));
        v.setAtivo(false);

        Veiculo salvo = repository.save(v);

        Optional<Veiculo> result = repository.findByIdAndAtivoTrue(salvo.getId());

        assertTrue(result.isEmpty());
    }
    // teste para validar o retorno do veiculo inativo
    @Test
    void naoDeveRetornarVeiculoInativo() {
        Veiculo v = new Veiculo();
        v.setMarca("Toyota");
        v.setModelo("Corolla");
        v.setAno(2021);
        v.setCor("Branco");
        v.setPlaca("XYZ9A88");
        v.setPrecoUsd(BigDecimal.valueOf(12000));
        v.setAtivo(false);

        Veiculo salvo = repository.save(v);


        repository.save(v);

        Optional<Veiculo> result = repository.findByIdAndAtivoTrue(v.getId());

        assertEquals(true, result.isEmpty());
    }

    // teste de relatorio por marca
    @Test
    void deveGerarRelatorioPorMarca() {
        Veiculo v1 = new Veiculo();
        v1.setMarca("Honda");
        v1.setModelo("Civic");
        v1.setAno(2021);
        v1.setCor("Branco");
        v1.setPrecoUsd(BigDecimal.valueOf(50000));
        v1.setAtivo(true);
        v1.setPlaca("AAA1B11");

        Veiculo v2 = new Veiculo();
        v2.setMarca("Honda");
        v2.setModelo("Fit");
        v2.setAno(2015);
        v2.setCor("Vermelho");
        v2.setPrecoUsd(BigDecimal.valueOf(50000));
        v2.setAtivo(true);
        v2.setPlaca("AAA1B12");

        repository.save(v1);
        repository.save(v2);

        List<MarcaRelatorioDTO> result = repository.findVeiculoCountByMarca();

        assertFalse(result.isEmpty());
        assertEquals("Honda", result.get(0).marca());
    }
}
