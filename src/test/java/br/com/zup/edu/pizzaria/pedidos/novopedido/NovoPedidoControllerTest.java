package br.com.zup.edu.pizzaria.pedidos.novopedido;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import br.com.zup.edu.pizzaria.pizzas.Pizza;
import br.com.zup.edu.pizzaria.pizzas.PizzaRepository;
import br.com.zup.edu.pizzaria.pizzas.cadastropizza.NovaPizzaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class NovoPedidoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    IngredienteRepository ingredienteRepository;

    @Autowired
    private PizzaRepository pizzaRepository;


    @Test
    @DisplayName("Cadastro Com Sucesso novo Pedido")
    public void cadastroComSucessoNovoPedido() throws Exception {

        Ingrediente ingrediente = new Ingrediente("Ovo", 1, new BigDecimal("1.0"));
        Ingrediente ingrediente1 = new Ingrediente("Tomate", 1, new BigDecimal("1.0"));
        ingredienteRepository.saveAll(List.of(ingrediente1, ingrediente));


        Pizza pizza = new Pizza("Ovo", new BigDecimal("20.0"), List.of(ingrediente1, ingrediente));




        MockHttpServletRequestBuilder request = post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(novaPizzaRequest));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/pedidos/{id}"));
    }

}