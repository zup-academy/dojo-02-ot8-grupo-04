package br.com.zup.edu.pizzaria.pizzas.cadastropizza;

import br.com.zup.edu.pizzaria.ingredientes.Ingrediente;
import br.com.zup.edu.pizzaria.pizzas.Pizza;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class PizzaTest {

    @Test
    void calcularPreco() {
        var ingrediente1 = new Ingrediente("Tomate", 1, new BigDecimal("1.99"));
        var ingrediente2 = new Ingrediente("Queijo", 1, new BigDecimal("1.00"));

        var pizza = new Pizza("4 Queijos", List.of(ingrediente1, ingrediente2));

        Assertions.assertEquals(new BigDecimal("22.99"), pizza.getPreco());
    }
}
