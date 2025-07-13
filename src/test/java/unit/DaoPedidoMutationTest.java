package unit;

import dao.DaoPedido;
import dao.DatabaseException;
import Model.Pedido;
import Model.Lanche;
import Model.Bebida;
import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("mutation")
public class DaoPedidoMutationTest {
    
    private DaoPedido daoPedido;
    
    @Mock
    private Pedido pedido;
    
    @Mock
    private Cliente cliente;
    
    @Mock
    private Lanche lanche;
    
    @Mock
    private Bebida bebida;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        daoPedido = new DaoPedido();
        
        // Injetar mock da conexão usando reflection
        java.lang.reflect.Field field = DaoPedido.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoPedido, mockConnection);
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComDadosValidos() throws Exception {
        // Arrange
        when(pedido.getCliente()).thenReturn(cliente);
        when(cliente.getId_cliente()).thenReturn(1);
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        when(pedido.getValor_total()).thenReturn(25.50);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        
        // Act
        daoPedido.salvar(pedido);
        
        // Assert - Verificar que todos os setters foram chamados na ordem correta
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setString(2, "2024-01-01");
        verify(mockPreparedStatement).setDouble(3, 25.50);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComExcecao() throws Exception {
        // Arrange
        when(pedido.getCliente()).thenReturn(cliente);
        when(cliente.getId_cliente()).thenReturn(1);
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        when(pedido.getValor_total()).thenReturn(25.50);
        
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            daoPedido.salvar(pedido);
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
        assertTrue(exception.getMessage().contains("Erro ao salvar pedido"));
    }
    
    @Test
    public void deveDetectarMutacaoVincularLancheComDadosValidos() throws Exception {
        // Arrange
        when(pedido.getId_pedido()).thenReturn(1);
        when(lanche.getId_lanche()).thenReturn(2);
        when(lanche.getQuantidade()).thenReturn(3);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        
        // Act
        daoPedido.vincularLanche(pedido, lanche);
        
        // Assert
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setInt(2, 2);
        verify(mockPreparedStatement).setInt(3, 3);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoVincularLancheComExcecao() throws Exception {
        // Arrange
        when(pedido.getId_pedido()).thenReturn(1);
        when(lanche.getId_lanche()).thenReturn(2);
        when(lanche.getQuantidade()).thenReturn(3);
        
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            daoPedido.vincularLanche(pedido, lanche);
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
        assertTrue(exception.getMessage().contains("Erro ao vincular lanche ao pedido"));
    }
    
    @Test
    public void deveDetectarMutacaoVincularBebidaComDadosValidos() throws Exception {
        // Arrange
        when(pedido.getId_pedido()).thenReturn(1);
        when(bebida.getId_bebida()).thenReturn(2);
        when(bebida.getQuantidade()).thenReturn(2);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        
        // Act
        daoPedido.vincularBebida(pedido, bebida);
        
        // Assert
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setInt(2, 2);
        verify(mockPreparedStatement).setInt(3, 2);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoVincularBebidaComExcecao() throws Exception {
        // Arrange
        when(pedido.getId_pedido()).thenReturn(1);
        when(bebida.getId_bebida()).thenReturn(2);
        when(bebida.getQuantidade()).thenReturn(2);
        
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            daoPedido.vincularBebida(pedido, bebida);
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
        assertTrue(exception.getMessage().contains("Erro ao vincular bebida ao pedido"));
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorDataComResultado() throws Exception {
        // Arrange
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Primeira chamada retorna true, segunda false
        when(mockResultSet.getInt("id_pedido")).thenReturn(1);
        when(mockResultSet.getString("data_pedido")).thenReturn("2024-01-01");
        when(mockResultSet.getDouble("valor_total")).thenReturn(25.50);
        
        // Act
        Pedido resultado = daoPedido.pesquisaPorData(pedido);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId_pedido());
        assertEquals("2024-01-01", resultado.getData_pedido());
        assertEquals(25.50, resultado.getValor_total());
        
        verify(mockPreparedStatement).setString(1, "2024-01-01");
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorDataSemResultado() throws Exception {
        // Arrange
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Nenhum resultado
        
        // Act
        Pedido resultado = daoPedido.pesquisaPorData(pedido);
        
        // Assert
        assertNotNull(resultado);
        // Deve retornar um pedido vazio (valores padrão)
        assertEquals(0, resultado.getId_pedido());
        assertNull(resultado.getData_pedido());
        assertNull(resultado.getValor_total());
        
        verify(mockPreparedStatement).setString(1, "2024-01-01");
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorDataComExcecao() throws Exception {
        // Arrange
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            daoPedido.pesquisaPorData(pedido);
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
        assertTrue(exception.getMessage().contains("Erro ao pesquisar pedido por data"));
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComPedidoNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.salvar(null);
        });
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComClienteNull() {
        // Arrange
        when(pedido.getCliente()).thenReturn(null);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.salvar(pedido);
        });
    }
    
    @Test
    public void deveDetectarMutacaoVincularLancheComPedidoNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.vincularLanche(null, lanche);
        });
    }
    
    @Test
    public void deveDetectarMutacaoVincularLancheComLancheNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.vincularLanche(pedido, null);
        });
    }
    
    @Test
    public void deveDetectarMutacaoVincularBebidaComPedidoNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.vincularBebida(null, bebida);
        });
    }
    
    @Test
    public void deveDetectarMutacaoVincularBebidaComBebidaNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.vincularBebida(pedido, null);
        });
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorDataComPedidoNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            daoPedido.pesquisaPorData(null);
        });
    }
    
    @Test
    public void deveDetectarMutacaoConstrutor() {
        // Act
        DaoPedido novoDao = new DaoPedido();
        
        // Assert
        assertNotNull(novoDao);
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorDataComMultiplosResultados() throws Exception {
        // Arrange
        when(pedido.getData_pedido()).thenReturn("2024-01-01");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Dois resultados
        when(mockResultSet.getInt("id_pedido")).thenReturn(1, 2);
        when(mockResultSet.getString("data_pedido")).thenReturn("2024-01-01", "2024-01-01");
        when(mockResultSet.getDouble("valor_total")).thenReturn(25.50, 30.00);
        
        // Act
        Pedido resultado = daoPedido.pesquisaPorData(pedido);
        
        // Assert - Deve retornar o último resultado processado
        assertNotNull(resultado);
        assertEquals(2, resultado.getId_pedido());
        assertEquals("2024-01-01", resultado.getData_pedido());
        assertEquals(30.00, resultado.getValor_total());
        
        verify(mockPreparedStatement).setString(1, "2024-01-01");
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next(); // Chamado 3 vezes: true, true, false
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
}