package br.com.zup.edu.pizzaria.pizzas.cadastropizza;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.ast.Var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovaPizzaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    List<Ingrediente> ingredientes;

    Ingrediente tomate = new Ingrediente("tomate", 1, new BigDecimal("1.99"));
    Ingrediente ovo = new Ingrediente("ovo", 1, new BigDecimal("1.00"));

    @BeforeEach
    public void start() {

        ingredientes = List.of(tomate, ovo);
    }

    @Test
    public void cadastroComSucesso() throws Exception {

        ingredientes = ingredienteRepository.saveAll(ingredientes);
        var ingredientesLista = ingredientes.stream().map(Ingrediente::getId).collect(Collectors.toList());
        NovaPizzaRequest novaPizzaRequest = new NovaPizzaRequest("Pizza de Ovo", ingredientesLista);


        MockHttpServletRequestBuilder request = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(novaPizzaRequest));

        mvc.perform(request)
                .andExpect(status().isOk());

    }


    @Test
    public void naoDeveCadastrarSemIngrediente() throws Exception {
        NovaPizzaRequest novaPizzaRequest = new NovaPizzaRequest("Pizza de Ovo", null);


        MockHttpServletRequestBuilder request = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(novaPizzaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

}


