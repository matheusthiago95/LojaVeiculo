package com.tinnova.lojaveiculo.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tinnova.lojaveiculo.dto.request.VeiculoCreateDTO;
import com.tinnova.lojaveiculo.dto.response.VeiculoResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VeiculoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Aqui é gerado o token
    private String getToken(String login, String senha) throws Exception {

        String body = """
                {
                    "login": "%s",
                    "senha": "%s"
                }
                """.formatted(login, senha);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        return JsonPath.read(json, "$.token");
    }

    // O usuário admin cria o veiculo
    @Test
    void adminDeveCriarVeiculo() throws Exception {

        String token = getToken("admin", "123456");

        mockMvc.perform(post("/veiculos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "marca": "Honda",
                    "modelo": "Civic",
                    "ano": 2020,
                    "cor": "Preto",
                    "placa": "ABC1D23",
                    "precoBrl": 10000
                }
            """))
                .andExpect(status().isCreated());
    }

    // O usuario user não pode criar o veiculo codigo 403
    @Test
    void userNaoPodeCriarVeiculo() throws Exception {

        String token = getToken("user", "123456");

        mockMvc.perform(post("/veiculos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "marca": "Honda",
                    "modelo": "Civic",
                    "ano": 2020,
                    "cor": "Preto",
                    "placa": "ABC1D23",
                    "precoBrl": 10000
                }
            """))
                .andExpect(status().isForbidden());
    }

    //Usuário não autenticado codigo 401
    @Test
    void userNaoAutenticado() throws Exception {

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
                "marca":"Honda",
                "modelo":"Civic",
                "ano":2020,
                "cor":"Preto",
                "placa":"ABC1D23",
                "precoBrl":10000
            }
            """))
                .andExpect(status().isUnauthorized());
    }

    //Usuario pode consultar
    @Test
    void userPodeListarVeiculos() throws Exception {

        String token = getToken("user", "123456");

        mockMvc.perform(get("/veiculos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    //Não pode cadastrar placa duplicada codigo 409
    @Test
    void naoDevePermitirPlacaDuplicada() throws Exception {

        String token = getToken("admin", "123456");

        String body = """
        {
            "marca": "Honda",
            "modelo": "Civic",
            "ano": 2020,
            "cor": "Preto",
            "placa": "ABC1D23",
            "precoBrl": 10000
        }
    """;

        mockMvc.perform(post("/veiculos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/veiculos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }



    // Delete do veiculo. Apenas o status ativo de true passa a ser false
    @Test
    void deveFazerSoftDelete() throws Exception {

        String token = getToken("admin", "123456");

        MvcResult result = mockMvc.perform(post("/veiculos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "marca": "Honda",
                    "modelo": "Civic",
                    "ano": 2020,
                    "cor": "Preto",
                    "placa": "ABC1D23",
                    "precoBrl": 10000
                }
            """))
                .andReturn();

        String json = result.getResponse().getContentAsString();

        VeiculoResponseDTO response =
                objectMapper.readValue(json, VeiculoResponseDTO.class);

        Long id = response.id();

        mockMvc.perform(delete("/veiculos/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/veiculos/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // fluxo do teste completo de ponta a ponta
    @Test
    void fluxoCompletoDoVeiculo() throws Exception {


        // 1 - Obter token ADMIN
        String token = getToken("admin", "123456");

        // 2 - Criar veículo
        VeiculoCreateDTO createDTO = new VeiculoCreateDTO(
                "Honda",
                "Civic Touring",
                2024,
                "Preto",
                "ABC1D23",
                BigDecimal.valueOf(150000)
        );

        MvcResult createResult = mockMvc.perform(post("/veiculos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marca").value("Honda"))
                .andExpect(jsonPath("$.modelo").value("Civic Touring"))
                .andReturn();

        VeiculoResponseDTO response = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                VeiculoResponseDTO.class);

        Long id = response.id();

        // 3 - Listar veículos

        mockMvc.perform(get("/veiculos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].placa")
                        .value("ABC1D23"));

        // 4 - Filtrar por marca
        mockMvc.perform(get("/veiculos")
                        .param("marca", "Honda")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].marca")
                        .value("Honda"));

        // 5 - Detalhar veículo
        mockMvc.perform(get("/veiculos/{id}", id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.placa").value("ABC1D23"))
                .andExpect(jsonPath("$.modelo").value("Civic Touring"));
    }




}
