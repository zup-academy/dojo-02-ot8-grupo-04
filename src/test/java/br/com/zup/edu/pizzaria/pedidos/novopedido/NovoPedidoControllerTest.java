package br.com.zup.edu.pizzaria.pedidos.novopedido;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import br.com.zup.edu.pizzaria.pedidos.TipoDeBorda;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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


        Pizza pizza = new Pizza("Ovo", List.of(ingrediente1, ingrediente));
        Pizza pizza1 = new Pizza("Morango", List.of(ingrediente1, ingrediente));


        pizzaRepository.saveAll(List.of(pizza, pizza1));

        EnderecoRequest enderecoRequest = new EnderecoRequest("Rua dois", "9","lote 2", "89000000" );

        ItemRequest itemRequest = new ItemRequest(pizza.getId(), TipoDeBorda.RECHEADA_CATUPIRY);
        ItemRequest itemRequest1 = new ItemRequest(pizza1.getId(), TipoDeBorda.RECHEADA_CHEDDAR);

        NovoPedidoRequest pedidoRequest = new NovoPedidoRequest(enderecoRequest, List.of(itemRequest,itemRequest1));


        MockHttpServletRequestBuilder request = post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pedidoRequest));

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/pedidos/{id}"));
    }

    @Test
    @DisplayName("Cadastro Pedido sem itens")
    public void cadastroPedidoSemItens() throws Exception {

        Ingrediente ingrediente = new Ingrediente("Ovo", 1, new BigDecimal("1.0"));
        Ingrediente ingrediente1 = new Ingrediente("Tomate", 1, new BigDecimal("1.0"));
        ingredienteRepository.saveAll(List.of(ingrediente1, ingrediente));


        Pizza pizza = new Pizza("Ovo", List.of(ingrediente1, ingrediente));
        Pizza pizza1 = new Pizza("Morango", List.of(ingrediente1, ingrediente));


        pizzaRepository.saveAll(List.of(pizza, pizza1));

        EnderecoRequest enderecoRequest = new EnderecoRequest("Rua dois", "9","lote 2", "89000000" );

        NovoPedidoRequest pedidoRequest = new NovoPedidoRequest(enderecoRequest, null);


        MockHttpServletRequestBuilder request = post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pedidoRequest))
                .locale(new Locale("pt","BR"));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("itens"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("n??o deve ser nulo"));

    }

    @Test
    @DisplayName("Cadastro Pedido sem Endereco")
    public void cadastroPedidoSemEndereco() throws Exception {

        Ingrediente ingrediente = new Ingrediente("Ovo", 1, new BigDecimal("1.0"));
        Ingrediente ingrediente1 = new Ingrediente("Tomate", 1, new BigDecimal("1.0"));
        ingredienteRepository.saveAll(List.of(ingrediente1, ingrediente));


        Pizza pizza = new Pizza("Ovo", List.of(ingrediente1, ingrediente));
        Pizza pizza1 = new Pizza("Morango", List.of(ingrediente1, ingrediente));


        pizzaRepository.saveAll(List.of(pizza, pizza1));


        ItemRequest itemRequest = new ItemRequest(pizza.getId(), TipoDeBorda.RECHEADA_CATUPIRY);
        ItemRequest itemRequest1 = new ItemRequest(pizza1.getId(), TipoDeBorda.RECHEADA_CHEDDAR);

        NovoPedidoRequest pedidoRequest = new NovoPedidoRequest(null, List.of(itemRequest,itemRequest1));


        MockHttpServletRequestBuilder request = post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pedidoRequest))
                .locale(new Locale("pt","BR"));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("endereco"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("n??o deve ser nulo"));

    }

}