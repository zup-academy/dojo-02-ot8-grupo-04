package br.com.zup.edu.pizzaria.pizzas.cadastropizza;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.ingredientes.IngredienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
    public void cadastroComSucesso() {

        ingredientes = ingredienteRepository.saveAll(ingredientes);
        NovaPizzaRequest novaPizzaRequest = new NovaPizzaRequest("Pizza de Ovo", List.of());
    }

}