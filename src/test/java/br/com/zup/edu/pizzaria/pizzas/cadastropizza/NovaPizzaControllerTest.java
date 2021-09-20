package br.com.zup.edu.pizzaria.pizzas.cadastropizza;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/pizzas/{id}"));
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

    @Test
    public void naoDeveCadastrarSemSabor() throws Exception {

        ingredientes = ingredienteRepository.saveAll(ingredientes);
        var ingredientesLista = ingredientes.stream().map(Ingrediente::getId).collect(Collectors.toList());

        NovaPizzaRequest novaPizzaRequest = new NovaPizzaRequest("", ingredientesLista);
        MockHttpServletRequestBuilder request = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(novaPizzaRequest)).locale(new Locale("pt", "BR"));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("sabor"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("não deve estar em branco"));
    }

    @Test
    public void naoDeveCadastrarComMenosDeUmIngrediente() throws Exception {
        NovaPizzaRequest novaPizzaRequest = new NovaPizzaRequest("Pizza de Ovo", new ArrayList<>());


        MockHttpServletRequestBuilder request = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .locale(new Locale("pt", "BR"))
                .content(new ObjectMapper().writeValueAsString(novaPizzaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("ingredientes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("tamanho deve ser entre 1 e 2147483647"));
    }

}


