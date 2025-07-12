package unit;

import dao.DaoFuncionario;
import Model.Funcionario;
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
public class DaoFuncionarioMutationTest {
    
    private DaoFuncionario daoFuncionario;
    
    @Mock
    private Funcionario funcionario;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        daoFuncionario = new DaoFuncionario();
        
        // Injetar mock da conexão usando reflection
        java.lang.reflect.Field field = DaoFuncionario.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoFuncionario, mockConnection);
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComDadosValidos() throws Exception {
        // Arrange
        when(funcionario.getNome()).thenReturn("João");
        when(funcionario.getSobrenome()).thenReturn("Silva");
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(funcionario.getSenha()).thenReturn("123456");
        when(funcionario.getCargo()).thenReturn("Gerente");
        when(funcionario.getSalario()).thenReturn(5000.0);
        when(funcionario.getCad_por()).thenReturn(1);
        when(funcionario.getFg_ativo()).thenReturn(1);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        
        // Act
        daoFuncionario.salvar(funcionario);
        
        // Assert - Verificar que todos os setters foram chamados na ordem correta
        verify(mockPreparedStatement).setString(1, "João");
        verify(mockPreparedStatement).setString(2, "Silva");
        verify(mockPreparedStatement).setString(3, "joao.silva");
        verify(mockPreparedStatement).setString(4, "123456");
        verify(mockPreparedStatement).setString(5, "Gerente");
        verify(mockPreparedStatement).setDouble(6, 5000.0);
        verify(mockPreparedStatement).setInt(7, 1);
        verify(mockPreparedStatement).setInt(8, 1);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComExcecao() throws Exception {
        // Arrange
        when(funcionario.getNome()).thenReturn("João");
        when(funcionario.getSobrenome()).thenReturn("Silva");
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(funcionario.getSenha()).thenReturn("123456");
        when(funcionario.getCargo()).thenReturn("Gerente");
        when(funcionario.getSalario()).thenReturn(5000.0);
        when(funcionario.getCad_por()).thenReturn(1);
        when(funcionario.getFg_ativo()).thenReturn(1);
        
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            daoFuncionario.salvar(funcionario);
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorUsuarioComResultado() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Primeira chamada retorna true, segunda false
        when(mockResultSet.getInt("id_funcionario")).thenReturn(1);
        when(mockResultSet.getString("nome")).thenReturn("João");
        when(mockResultSet.getString("sobrenome")).thenReturn("Silva");
        when(mockResultSet.getString("usuario")).thenReturn("joao.silva");
        when(mockResultSet.getString("senha")).thenReturn("senha_hash");
        when(mockResultSet.getDouble("salario")).thenReturn(5000.0);
        when(mockResultSet.getInt("cad_por")).thenReturn(1);
        
        // Act
        Funcionario resultado = daoFuncionario.pesquisaPorUsuario(funcionario);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("João", resultado.getNome());
        assertEquals("Silva", resultado.getSobrenome());
        assertEquals("joao.silva", resultado.getUsuario());
        assertEquals("senha_hash", resultado.getSenha());
        assertEquals(5000.0, resultado.getSalario());
        assertEquals(1, resultado.getCad_por());
        assertEquals(1, resultado.getFg_ativo()); // Sempre deve ser 1
        
        // O método pesquisaPorUsuario não usa setString, usa concatenação direta na query
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorUsuarioSemResultado() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("usuario_inexistente");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Nenhum resultado
        
        // Act
        Funcionario resultado = daoFuncionario.pesquisaPorUsuario(funcionario);
        
        // Assert
        assertNotNull(resultado);
        // Deve retornar um funcionário vazio (valores padrão)
        assertEquals(0, resultado.getId());
        assertNull(resultado.getNome());
        assertNull(resultado.getSobrenome());
        assertNull(resultado.getUsuario());
        assertNull(resultado.getSenha());
        assertNull(resultado.getSalario()); // Pode ser null se não foi setado
        assertEquals(0, resultado.getCad_por());
        assertEquals(0, resultado.getFg_ativo()); // Valor padrão quando não há resultado
        
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorUsuarioComExcecao() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            daoFuncionario.pesquisaPorUsuario(funcionario);
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }
    
    @Test
    public void deveDetectarMutacaoLoginComSucesso() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(funcionario.getSenha()).thenReturn("123456");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("usuario")).thenReturn("joao.silva");
        when(mockResultSet.getString("senha")).thenReturn("e10adc3949ba59abbe56e057f20f883e"); // MD5 de "123456"
        when(mockResultSet.getInt("fg_ativo")).thenReturn(1);
        
        // Act
        boolean resultado = daoFuncionario.login(funcionario);
        
        // Assert
        assertTrue(resultado, "Login deve ser bem-sucedido com credenciais válidas e funcionário ativo");
        
        verify(mockPreparedStatement).setString(1, "joao.silva");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoLoginComSenhaIncorreta() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(funcionario.getSenha()).thenReturn("senha_errada");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("usuario")).thenReturn("joao.silva");
        when(mockResultSet.getString("senha")).thenReturn("e10adc3949ba59abbe56e057f20f883e"); // MD5 de "123456"
        when(mockResultSet.getInt("fg_ativo")).thenReturn(1);
        
        // Act
        boolean resultado = daoFuncionario.login(funcionario);
        
        // Assert
        assertFalse(resultado, "Login deve falhar com senha incorreta");
        
        verify(mockPreparedStatement).setString(1, "joao.silva");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoLoginComFuncionarioInativo() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(funcionario.getSenha()).thenReturn("123456");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("usuario")).thenReturn("joao.silva");
        when(mockResultSet.getString("senha")).thenReturn("e10adc3949ba59abbe56e057f20f883e"); // MD5 de "123456"
        when(mockResultSet.getInt("fg_ativo")).thenReturn(0); // Funcionário inativo
        
        // Act
        boolean resultado = daoFuncionario.login(funcionario);
        
        // Assert
        assertFalse(resultado, "Login deve falhar com funcionário inativo");
        
        verify(mockPreparedStatement).setString(1, "joao.silva");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoLoginSemResultado() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("usuario_inexistente");
        when(funcionario.getSenha()).thenReturn("123456");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Nenhum resultado
        
        // Act
        boolean resultado = daoFuncionario.login(funcionario);
        
        // Assert
        assertFalse(resultado, "Login deve falhar com usuário inexistente");
        
        verify(mockPreparedStatement).setString(1, "usuario_inexistente");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }
    
    @Test
    public void deveDetectarMutacaoLoginComExcecao() throws Exception {
        // Arrange
        when(funcionario.getUsuario()).thenReturn("joao.silva");
        when(funcionario.getSenha()).thenReturn("123456");
        
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        
        // Act
        boolean resultado = daoFuncionario.login(funcionario);
        
        // Assert
        assertFalse(resultado, "Login deve retornar false quando há exceção");
    }
    
    @Test
    public void deveDetectarMutacaoSalvarComFuncionarioNull() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            daoFuncionario.salvar(null);
        });
        
        assertNotNull(exception);
        assertTrue(exception.getCause() instanceof NullPointerException);
    }
    
    @Test
    public void deveDetectarMutacaoPesquisaPorUsuarioComFuncionarioNull() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            daoFuncionario.pesquisaPorUsuario(null);
        });
        
        assertNotNull(exception);
        // O método pesquisaPorUsuario pode não ter NullPointerException como causa direta
        // devido à concatenação de string na query SQL
    }
    
    @Test
    public void deveDetectarMutacaoLoginComFuncionarioNull() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            daoFuncionario.login(null);
        });
        
        assertNotNull(exception);
    }
    
    @Test
    public void deveDetectarMutacaoConstrutor() {
        // Act
        DaoFuncionario novoDao = new DaoFuncionario();
        
        // Assert
        assertNotNull(novoDao, "Construtor deve criar uma instância válida");
    }
} 