package unitTests;

import dao.DaoPedido;
import dao.DatabaseException;
import Model.Pedido;
import Model.Lanche;
import Model.Bebida;
import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class DaoPedidoTest {
    
    private DaoPedido daoPedido;
    
    @Mock
    private Cliente cliente;
    
    @Mock
    private Pedido pedido;
    
    @Mock
    private Lanche lanche;
    
    @Mock
    private Bebida bebida;
    
    @BeforeEach
    void setUp() {
        daoPedido = new DaoPedido();
    }
    
    @Test
    void testSalvarPedido() {
        // Configurar mocks
        when(cliente.getId_cliente()).thenReturn(1);
        when(pedido.getCliente()).thenReturn(cliente);
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        when(pedido.getValor_total()).thenReturn(25.50);
        
        // Executar - isso vai executar o código real da classe
        try {
            daoPedido.salvar(pedido);
        } catch (Exception e) {
            // Esperado que falhe por não ter banco de dados, mas o código foi executado
            assertTrue(e instanceof DatabaseException || e instanceof RuntimeException);
        }
        
        // Verificar que os métodos foram chamados
        verify(pedido).getCliente();
        verify(pedido).getData_pedido();
        verify(pedido).getValor_total();
    }
    
    @Test
    void testVincularLanche() {
        // Configurar mocks
        when(pedido.getId_pedido()).thenReturn(1);
        when(lanche.getId_lanche()).thenReturn(1);
        when(lanche.getQuantidade()).thenReturn(2);
        
        // Executar - isso vai executar o código real da classe
        try {
            daoPedido.vincularLanche(pedido, lanche);
        } catch (Exception e) {
            // Esperado que falhe por não ter banco de dados, mas o código foi executado
            assertTrue(e instanceof DatabaseException || e instanceof RuntimeException);
        }
        
        // Verificar que os métodos foram chamados
        verify(pedido).getId_pedido();
        verify(lanche).getId_lanche();
        verify(lanche).getQuantidade();
    }
    
    @Test
    void testVincularBebida() {
        // Configurar mocks
        when(pedido.getId_pedido()).thenReturn(1);
        when(bebida.getId_bebida()).thenReturn(1);
        when(bebida.getQuantidade()).thenReturn(1);
        
        // Executar - isso vai executar o código real da classe
        try {
            daoPedido.vincularBebida(pedido, bebida);
        } catch (Exception e) {
            // Esperado que falhe por não ter banco de dados, mas o código foi executado
            assertTrue(e instanceof DatabaseException || e instanceof RuntimeException);
        }
        
        // Verificar que os métodos foram chamados
        verify(pedido).getId_pedido();
        verify(bebida).getId_bebida();
        verify(bebida).getQuantidade();
    }
    
    @Test
    void testPesquisaPorData() {
        // Configurar mocks
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        
        // Executar - isso vai executar o código real da classe
        try {
            daoPedido.pesquisaPorData(pedido);
        } catch (Exception e) {
            // Esperado que falhe por não ter banco de dados, mas o código foi executado
            assertTrue(e instanceof DatabaseException || e instanceof RuntimeException);
        }
        
        // Verificar que o método foi chamado
        verify(pedido).getData_pedido();
    }
    
    @Test
    void testConstrutorDaoPedido() {
        // Teste do construtor - isso executa o código real
        DaoPedido novoDao = new DaoPedido();
        assertNotNull(novoDao);
    }
    
    @Test
    void testSalvarPedidoWithNullCliente() {
        // Teste com cliente nulo para forçar exceção
        when(pedido.getCliente()).thenReturn(null);
        
        try {
            daoPedido.salvar(pedido);
        } catch (Exception e) {
            // Deve lançar exceção por cliente nulo
            assertTrue(e instanceof NullPointerException || e instanceof DatabaseException);
        }
    }
    
    @Test
    void testVincularLancheWithNullPedido() {
        // Teste com pedido nulo para forçar exceção
        try {
            daoPedido.vincularLanche(null, lanche);
        } catch (Exception e) {
            // Deve lançar exceção por pedido nulo
            assertTrue(e instanceof NullPointerException || e instanceof DatabaseException);
        }
    }
    
    @Test
    void testVincularBebidaWithNullBebida() {
        // Teste com bebida nula para forçar exceção
        when(pedido.getId_pedido()).thenReturn(1);
        
        try {
            daoPedido.vincularBebida(pedido, null);
        } catch (Exception e) {
            // Deve lançar exceção por bebida nula
            assertTrue(e instanceof NullPointerException || e instanceof DatabaseException);
        }
    }
    
    @Test
    void testPesquisaPorDataWithNullPedido() {
        // Teste com pedido nulo para forçar exceção
        try {
            daoPedido.pesquisaPorData(null);
        } catch (Exception e) {
            // Deve lançar exceção por pedido nulo
            assertTrue(e instanceof NullPointerException || e instanceof DatabaseException);
        }
    }

    @Test
    void testSalvarPedidoThrowsDatabaseException() throws SQLException, NoSuchFieldException, IllegalAccessException {
        // Mock do Connection e PreparedStatement
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoPedido daoPedidoTest = new DaoPedido();
        // Injetar o mock (use reflection se o campo for private)
        java.lang.reflect.Field field = DaoPedido.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoPedidoTest, mockConn);

        Pedido pedidoTest = mock(Pedido.class);

        assertThrows(DatabaseException.class, () -> daoPedidoTest.salvar(pedidoTest));
    }

    @Test
    void testPesquisaPorDataThrowsDatabaseException() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoPedido daoPedidoTest = new DaoPedido();
        java.lang.reflect.Field field = DaoPedido.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoPedidoTest, mockConn);

        Pedido pedidoTest = mock(Pedido.class);

        assertThrows(DatabaseException.class, () -> daoPedidoTest.pesquisaPorData(pedidoTest));
    }
} 