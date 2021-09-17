package br.com.zup.edu.pizzaria.pedidos.novopedido;

import br.com.zup.edu.pizzaria.pedidos.Pedido;
import br.com.zup.edu.pizzaria.pizzas.PizzaRepository;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class NovoPedidoRequest {

    @NotNull
    private EnderecoRequest endereco;

    @NotNull
    private List<ItemRequest> itens;

    @JsonCreator(mode = PROPERTIES)
    public NovoPedidoRequest(@NotNull EnderecoRequest endereco,
                             @NotNull List<ItemRequest> itens) {
        this.endereco = endereco;
        this.itens = itens;
    }


    public Pedido paraPedido(PizzaRepository repository) {

        Pedido pedido = new Pedido(endereco.paraEndereco());

        itens.stream()
             .map(item -> item.paraItem(pedido, repository))
             .forEach(pedido::adicionarItem);


        return pedido;
    }


    public EnderecoRequest getEndereco() {
        return endereco;
    }

    public List<ItemRequest> getItens() {
        return itens;
    }
}
