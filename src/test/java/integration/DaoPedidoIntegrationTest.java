package integration;

import dao.DaoPedido;
import Model.Pedido;
import Model.Lanche;
import Model.Bebida;
import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class DaoPedidoIntegrationTest {
    
    private DaoPedido daoPedido;
    
    @BeforeEach
    void setUp() {
        daoPedido = new DaoPedido();
    }
    
    @Test
    @Disabled("Teste desabilitado para evitar erros de banco de dados em ambiente de CI/CD")
    void testSalvarPedidoIntegration() {
        // Criar objetos reais (não mocks)
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData_pedido("2024-01-01");
        pedido.setValor_total(25.50);
        
        // Executar o código real
        daoPedido.salvar(pedido);
    }
    
    @Test
    @Disabled("Teste desabilitado para evitar erros de banco de dados em ambiente de CI/CD")
    void testVincularLancheIntegration() {
        // Criar objetos reais
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        
        Lanche lanche = new Lanche();
        lanche.setId_lanche(1);
        lanche.setQuantidade(2);
        
        // Executar o código real
        daoPedido.vincularLanche(pedido, lanche);
    }
    
    @Test
    @Disabled("Teste desabilitado para evitar erros de banco de dados em ambiente de CI/CD")
    void testVincularBebidaIntegration() {
        // Criar objetos reais
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        
        Bebida bebida = new Bebida();
        bebida.setId_bebida(1);
        bebida.setQuantidade(1);
        
        // Executar o código real
        daoPedido.vincularBebida(pedido, bebida);
    }
    
    @Test
    @Disabled("Teste desabilitado para evitar erros de banco de dados em ambiente de CI/CD")
    void testPesquisaPorDataIntegration() {
        // Criar objeto real
        Pedido pedido = new Pedido();
        pedido.setData_pedido("2024-01-01");
        
        // Executar o código real
        daoPedido.pesquisaPorData(pedido);
    }
} 