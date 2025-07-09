package integrationTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import DAO.DaoLanche;
import DAO.DaoBebida;
import DAO.DaoPedido;
import DAO.DaoCliente;
import Model.Lanche;
import Model.Bebida;
import Model.Pedido;
import Model.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Testes de integração para funcionalidades de compra
 * Testa a integração real com o banco de dados
 */
@Tag("integration")
public class CompraIntegrationTest extends BaseIntegrationTest {
    
    private DaoCliente daoCliente;
    private DaoLanche daoLanche;
    private DaoBebida daoBebida;
    private DaoPedido daoPedido;
    
    @BeforeEach
    void setUp() throws Exception {
        setUpIntegration();
        daoCliente = new DaoCliente();
        daoLanche = new DaoLanche();
        daoBebida = new DaoBebida();
        daoPedido = new DaoPedido();
        inserirDadosTeste();
        inserirLancheTeste();
    }
    
    private void inserirLancheTeste() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            // Inserir lanche de teste
            stmt.execute("INSERT INTO tb_lanches (nm_lanche, descricao, valor_venda, fg_ativo) " +
                        "VALUES ('X-Burger', 'Hambúrguer com queijo', 15.00, 1)");
            
            // Vincular ingredientes ao lanche
            stmt.execute("INSERT INTO tb_ingredientes_lanche (id_lanche, id_ingrediente, quantidade) " +
                        "VALUES (1, 1, 1)"); // 1 pão
            stmt.execute("INSERT INTO tb_ingredientes_lanche (id_lanche, id_ingrediente, quantidade) " +
                        "VALUES (1, 2, 1)"); // 1 hambúrguer
        }
    }
    
    @Test
    void testCriarPedidoComLanche() throws Exception {
        // Arrange
        Pedido pedido = new Pedido();
        Cliente cliente = daoCliente.pesquisaPorID("1");
        pedido.setCliente(cliente);
        pedido.setValor_total(15.00);
        pedido.setData_pedido(java.time.Instant.now().toString());
        
        // Act
        daoPedido.salvar(pedido);
        
        // Buscar o pedido salvo
        Pedido pedidoSalvo = daoPedido.pesquisaPorData(pedido);
        assertNotNull(pedidoSalvo, "Pedido deveria ter sido salvo");
        
        // Vincular lanche ao pedido
        Lanche lanche = daoLanche.pesquisaPorNome("X-Burger");
        assertNotNull(lanche, "Lanche deveria ser encontrado");
        
        daoPedido.vincularLanche(pedidoSalvo, lanche);
        
        // Assert
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_lanches_pedido WHERE id_pedido = " + pedidoSalvo.getId_pedido());
            rs.next();
            assertEquals(1, rs.getInt(1), "Lanche deveria estar vinculado ao pedido");
        }
    }
    
    @Test
    void testCriarPedidoComBebida() throws Exception {
        // Arrange
        Pedido pedido = new Pedido();
        Cliente cliente = daoCliente.pesquisaPorID("1");
        pedido.setCliente(cliente);
        pedido.setValor_total(5.00);
        pedido.setData_pedido(java.time.Instant.now().toString());
        
        // Act
        daoPedido.salvar(pedido);
        
        // Buscar o pedido salvo
        Pedido pedidoSalvo = daoPedido.pesquisaPorData(pedido);
        assertNotNull(pedidoSalvo, "Pedido deveria ter sido salvo");
        
        // Vincular bebida ao pedido
        Bebida bebida = daoBebida.pesquisaPorNome("Coca-Cola");
        assertNotNull(bebida, "Bebida deveria ser encontrada");
        
        daoPedido.vincularBebida(pedidoSalvo, bebida);
        
        // Assert
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_bebidas_pedido WHERE id_pedido = " + pedidoSalvo.getId_pedido());
            rs.next();
            assertEquals(1, rs.getInt(1), "Bebida deveria estar vinculada ao pedido");
        }
    }
    
    @Test
    void testCriarPedidoComLancheEBebida() throws Exception {
        // Arrange
        Pedido pedido = new Pedido();
        Cliente cliente = daoCliente.pesquisaPorID("1");
        pedido.setCliente(cliente);
        pedido.setValor_total(20.00); // 15.00 + 5.00
        pedido.setData_pedido(java.time.Instant.now().toString());
        
        // Act
        daoPedido.salvar(pedido);
        
        // Buscar o pedido salvo
        Pedido pedidoSalvo = daoPedido.pesquisaPorData(pedido);
        assertNotNull(pedidoSalvo, "Pedido deveria ter sido salvo");
        
        // Vincular lanche e bebida ao pedido
        Lanche lanche = daoLanche.pesquisaPorNome("X-Burger");
        Bebida bebida = daoBebida.pesquisaPorNome("Coca-Cola");
        
        daoPedido.vincularLanche(pedidoSalvo, lanche);
        daoPedido.vincularBebida(pedidoSalvo, bebida);
        
        // Assert
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_lanches_pedido WHERE id_pedido = " + pedidoSalvo.getId_pedido());
            rs.next();
            assertEquals(1, rs.getInt(1), "Lanche deveria estar vinculado ao pedido");
            
            rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_bebidas_pedido WHERE id_pedido = " + pedidoSalvo.getId_pedido());
            rs.next();
            assertEquals(1, rs.getInt(1), "Bebida deveria estar vinculada ao pedido");
        }
    }
    
    @Test
    void testBuscarClientePorID() throws Exception {
        // Act
        Cliente cliente = daoCliente.pesquisaPorID("1");
        
        // Assert
        assertNotNull(cliente, "Cliente deveria ser encontrado");
        assertEquals("Cliente", cliente.getNome());
        assertEquals("Teste", cliente.getSobrenome());
    }
    
    @Test
    void testBuscarLanchePorNome() throws Exception {
        // Act
        Lanche lanche = daoLanche.pesquisaPorNome("X-Burger");
        
        // Assert
        assertNotNull(lanche, "Lanche deveria ser encontrado");
        assertEquals("X-Burger", lanche.getNome());
        assertEquals(15.00, lanche.getValor_venda());
    }
    
    @Test
    void testBuscarBebidaPorNome() throws Exception {
        // Act
        Bebida bebida = daoBebida.pesquisaPorNome("Coca-Cola");
        
        // Assert
        assertNotNull(bebida, "Bebida deveria ser encontrada");
        assertEquals("Coca-Cola", bebida.getNome());
        assertEquals(5.00, bebida.getValor_venda());
    }
    
    @Test
    void testBuscarProdutoInexistente() throws Exception {
        // Act
        Lanche lanche = daoLanche.pesquisaPorNome("ProdutoInexistente");
        Bebida bebida = daoBebida.pesquisaPorNome("BebidaInexistente");
        
        // Assert
        assertNull(lanche, "Lanche não deveria ser encontrado");
        assertNull(bebida, "Bebida não deveria ser encontrada");
    }
    
    @Test
    void testListarTodosLanches() throws Exception {
        // Act
        List<Lanche> lanches = daoLanche.listarTodos();
        
        // Assert
        assertNotNull(lanches, "Lista não deveria ser nula");
        assertTrue(lanches.size() > 0, "Deveria haver pelo menos um lanche");
        
        // Verificar se todos os lanches estão ativos
        lanches.forEach(lanche -> 
            assertEquals(1, lanche.getFg_ativo(), "Todos os lanches deveriam estar ativos")
        );
    }
    
    @Test
    void testListarTodasBebidas() throws Exception {
        // Act
        List<Bebida> bebidas = daoBebida.listarTodos();
        
        // Assert
        assertNotNull(bebidas, "Lista não deveria ser nula");
        assertTrue(bebidas.size() > 0, "Deveria haver pelo menos uma bebida");
        
        // Verificar se todas as bebidas estão ativas
        bebidas.forEach(bebida -> 
            assertEquals(1, bebida.getFg_ativo(), "Todas as bebidas deveriam estar ativas")
        );
    }
} 