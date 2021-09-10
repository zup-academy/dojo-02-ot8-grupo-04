package br.com.zup.edu.pizzaria.ingredientes.cadastrodeingredientes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovoIngredienteControllerTest {


    @Autowired
    private MockMvc mvc;

    @Test
    void deveCadastrarNovoIngrediente() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request)
           .andExpect(status().isCreated())
           .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/ingredientes/*"));

    }

    @Test
    void naoDeveCadastrarNovoIngredienteSemNome() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void naoDeveCadastrarNovoIngredientePrecoZero() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("0.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void naoDeveCadastrarIngredienteComValorNegativo() throws Exception {

        NovoIngredienteRequest body = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("-2.0"), 200);
        MockHttpServletRequestBuilder request = post("/api/ingredientes")
                .locale(new Locale("pt", "BR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(body));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("preco"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("deve ser maior que 0"))
                .andReturn().getResponse();

    }

}