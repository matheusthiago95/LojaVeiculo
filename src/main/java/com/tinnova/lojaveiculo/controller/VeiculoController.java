package com.tinnova.lojaveiculo.controller;

import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoPatchDTO;
import com.tinnova.lojaveiculo.dto.request.VeiculoUpdateDTO;
import com.tinnova.lojaveiculo.dto.response.MarcaRelatorioDTO;
import com.tinnova.lojaveiculo.dto.response.VeiculoResponseDTO;
import com.tinnova.lojaveiculo.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
@Tag(
        name = "Veículos",
        description = "API responsável pelo gerenciamento dos veículos"
)
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Operation(
            summary = "Listar veículos",
            description = "Retorna uma lista paginada de veículos permitindo filtros opcionais."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<VeiculoResponseDTO>> listarVeiculos(

            @RequestParam(required = false) String marca,

            @RequestParam(required = false) Integer ano,

            @RequestParam(required = false) String cor,

            @RequestParam(required = false) BigDecimal minPreco,

            @RequestParam(required = false) BigDecimal maxPreco,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                veiculoService.listarVeiculos(
                        marca,
                        ano,
                        cor,
                        minPreco,
                        maxPreco,
                        pageable));
    }

    @Operation(
            summary = "Buscar veículo por ID",
            description = "Retorna os dados de um veículo específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> detalharVeiculo(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                veiculoService.detalharVeiculo(id));
    }

    @Operation(
            summary = "Cadastrar veículo",
            description = "Realiza o cadastro de um novo veículo. Disponível apenas para usuários com perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso permitido apenas para ADMIN"),
            @ApiResponse(responseCode = "409", description = "Placa já cadastrada")
    })
    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> adicionarVeiculo(
            @Valid @RequestBody VeiculoCreateDTO dto) {

        VeiculoResponseDTO response =
                veiculoService.adicionarVeiculo(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Atualizar veículo",
            description = "Atualiza completamente os dados de um veículo. Disponível apenas para usuários com perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo atualizado"),
            @ApiResponse(responseCode = "403", description = "Acesso permitido apenas para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> atualizarVeiculo(
            @PathVariable Long id,
            @Valid @RequestBody VeiculoUpdateDTO dto) {

        return ResponseEntity.ok(
                veiculoService.atualizarVeiculo(id, dto));
    }

    @Operation(
            summary = "Atualizar parcialmente veículo",
            description = "Atualiza apenas os campos enviados na requisição. Disponível apenas para usuários com perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo atualizado"),
            @ApiResponse(responseCode = "403", description = "Acesso permitido apenas para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> atualizarParcialmenteVeiculo(
            @PathVariable Long id,
            @Valid @RequestBody VeiculoPatchDTO dto) {

        return ResponseEntity.ok(
                veiculoService.atualizarParcialmenteVeiculo(id, dto));
    }

    @Operation(
            summary = "Remover veículo",
            description = "Realiza a exclusão lógica (Soft Delete) de um veículo. Disponível apenas para usuários com perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veículo removido"),
            @ApiResponse(responseCode = "403", description = "Acesso permitido apenas para ADMIN"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerVeiculo(
            @PathVariable Long id) {

        veiculoService.removerVeiculo(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Relatório por marca",
            description = "Retorna a quantidade de veículos cadastrados por marca."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    @GetMapping("/relatorios/por-marca")
    public ResponseEntity<List<MarcaRelatorioDTO>> gerarRelatorioPorMarca() {

        return ResponseEntity.ok(
                veiculoService.gerarRelatorioPorMarca());
    }

}