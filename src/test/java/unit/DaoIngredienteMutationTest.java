package unit;

import dao.DaoIngrediente;
import Model.Ingrediente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("mutation")
public class DaoIngredienteMutationTest {
    private DaoIngrediente daoIngrediente;
    @Mock private Ingrediente ingrediente;
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        daoIngrediente = new DaoIngrediente();
        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngrediente, mockConnection);
    }

    @Test
    public void deveDetectarMutacaoSalvarComDadosValidos() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo");
        when(ingrediente.getDescricao()).thenReturn("Queijo cheddar");
        when(ingrediente.getQuantidade()).thenReturn(100);
        when(ingrediente.getValor_compra()).thenReturn(5.0);
        when(ingrediente.getValor_venda()).thenReturn(8.0);
        when(ingrediente.getTipo()).thenReturn("Laticínio");
        when(ingrediente.getFg_ativo()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        daoIngrediente.salvar(ingrediente);
        verify(mockPreparedStatement).setString(1, "Queijo");
        verify(mockPreparedStatement).setString(2, "Queijo cheddar");
        verify(mockPreparedStatement).setInt(3, 100);
        verify(mockPreparedStatement).setDouble(4, 5.0);
        verify(mockPreparedStatement).setDouble(5, 8.0);
        verify(mockPreparedStatement).setString(6, "Laticínio");
        verify(mockPreparedStatement).setInt(7, 1);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoSalvarComExcecao() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo");
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.salvar(ingrediente));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    public void deveDetectarMutacaoSalvarComIngredienteNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.salvar(null));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof NullPointerException);
    }

    @Test
    public void deveDetectarMutacaoListarTodosComResultado() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id_ingrediente")).thenReturn(1);
        when(mockResultSet.getString("nm_ingrediente")).thenReturn("Queijo");
        when(mockResultSet.getString("descricao")).thenReturn("Queijo cheddar");
        when(mockResultSet.getInt("quantidade")).thenReturn(100);
        when(mockResultSet.getDouble("valor_compra")).thenReturn(5.0);
        when(mockResultSet.getDouble("valor_venda")).thenReturn(8.0);
        when(mockResultSet.getString("tipo")).thenReturn("Laticínio");
        List<Ingrediente> lista = daoIngrediente.listarTodos();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        Ingrediente ing = lista.get(0);
        assertEquals(1, ing.getId_ingrediente());
        assertEquals("Queijo", ing.getNome());
        assertEquals("Queijo cheddar", ing.getDescricao());
        assertEquals(100, ing.getQuantidade());
        assertEquals(5.0, ing.getValor_compra());
        assertEquals(8.0, ing.getValor_venda());
        assertEquals("Laticínio", ing.getTipo());
        assertEquals(1, ing.getFg_ativo());
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoListarTodosSemResultado() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        List<Ingrediente> lista = daoIngrediente.listarTodos();
        assertNotNull(lista);
        assertEquals(0, lista.size());
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoListarTodosComExcecao() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.listarTodos());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    public void deveDetectarMutacaoListarTodosPorLancheComResultado() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id_ingrediente")).thenReturn(1);
        when(mockResultSet.getString("nm_ingrediente")).thenReturn("Queijo");
        when(mockResultSet.getString("descricao")).thenReturn("Queijo cheddar");
        when(mockResultSet.getInt("quantidade")).thenReturn(100);
        when(mockResultSet.getDouble("valor_compra")).thenReturn(5.0);
        when(mockResultSet.getDouble("valor_venda")).thenReturn(8.0);
        when(mockResultSet.getString("tipo")).thenReturn("Laticínio");
        List<Ingrediente> lista = daoIngrediente.listarTodosPorLanche(1);
        assertNotNull(lista);
        assertEquals(1, lista.size());
        Ingrediente ing = lista.get(0);
        assertEquals(1, ing.getId_ingrediente());
        assertEquals("Queijo", ing.getNome());
        assertEquals("Queijo cheddar", ing.getDescricao());
        assertEquals(100, ing.getQuantidade());
        assertEquals(5.0, ing.getValor_compra());
        assertEquals(8.0, ing.getValor_venda());
        assertEquals("Laticínio", ing.getTipo());
        assertEquals(1, ing.getFg_ativo());
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoListarTodosPorLancheSemResultado() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        List<Ingrediente> lista = daoIngrediente.listarTodosPorLanche(1);
        assertNotNull(lista);
        assertEquals(0, lista.size());
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoListarTodosPorLancheComExcecao() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.listarTodosPorLanche(1));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    public void deveDetectarMutacaoAlterarComDadosValidos() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo Atualizado");
        when(ingrediente.getDescricao()).thenReturn("Queijo cheddar atualizado");
        when(ingrediente.getQuantidade()).thenReturn(150);
        when(ingrediente.getValor_compra()).thenReturn(6.0);
        when(ingrediente.getValor_venda()).thenReturn(9.0);
        when(ingrediente.getTipo()).thenReturn("Laticínio");
        when(ingrediente.getId_ingrediente()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        daoIngrediente.alterar(ingrediente);
        verify(mockPreparedStatement).setString(1, "Queijo Atualizado");
        verify(mockPreparedStatement).setString(2, "Queijo cheddar atualizado");
        verify(mockPreparedStatement).setInt(3, 150);
        verify(mockPreparedStatement).setDouble(4, 6.0);
        verify(mockPreparedStatement).setDouble(5, 9.0);
        verify(mockPreparedStatement).setString(6, "Laticínio");
        verify(mockPreparedStatement).setInt(7, 1);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoAlterarComExcecao() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo Atualizado");
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.alterar(ingrediente));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    public void deveDetectarMutacaoAlterarComIngredienteNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.alterar(null));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof NullPointerException);
    }

    @Test
    public void deveDetectarMutacaoRemoverComDadosValidos() throws Exception {
        when(ingrediente.getId_ingrediente()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);
        daoIngrediente.remover(ingrediente);
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).execute();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoRemoverComExcecao() throws Exception {
        when(ingrediente.getId_ingrediente()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.remover(ingrediente));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    public void deveDetectarMutacaoRemoverComIngredienteNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.remover(null));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof NullPointerException);
    }

    @Test
    public void deveDetectarMutacaoPesquisaPorNomeComResultado() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id_ingrediente")).thenReturn(1);
        when(mockResultSet.getString("nm_ingrediente")).thenReturn("Queijo");
        when(mockResultSet.getString("descricao")).thenReturn("Queijo cheddar");
        when(mockResultSet.getInt("quantidade")).thenReturn(100);
        when(mockResultSet.getDouble("valor_compra")).thenReturn(5.0);
        when(mockResultSet.getDouble("valor_venda")).thenReturn(8.0);
        when(mockResultSet.getString("tipo")).thenReturn("Laticínio");
        Ingrediente resultado = daoIngrediente.pesquisaPorNome(ingrediente);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId_ingrediente());
        assertEquals("Queijo", resultado.getNome());
        assertEquals("Queijo cheddar", resultado.getDescricao());
        assertEquals(100, resultado.getQuantidade());
        assertEquals(5.0, resultado.getValor_compra());
        assertEquals(8.0, resultado.getValor_venda());
        assertEquals("Laticínio", resultado.getTipo());
        assertEquals(1, resultado.getFg_ativo());
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoPesquisaPorNomeSemResultado() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        Ingrediente resultado = daoIngrediente.pesquisaPorNome(ingrediente);
        assertNotNull(resultado);
        assertEquals(0, resultado.getId_ingrediente());
        assertNull(resultado.getNome());
        assertNull(resultado.getDescricao());
        assertEquals(0, resultado.getQuantidade());
        assertNull(resultado.getValor_compra()); // Aceitar null
        assertNull(resultado.getValor_venda());  // Aceitar null
        assertNull(resultado.getTipo());
        assertEquals(0, resultado.getFg_ativo()); // Valor padrão quando não há resultado
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();
    }

    @Test
    public void deveDetectarMutacaoPesquisaPorNomeComExcecao() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo");
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Erro de banco"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> daoIngrediente.pesquisaPorNome(ingrediente));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
    }

    @Test
    public void deveDetectarMutacaoPesquisaPorNomeComIngredienteNull() {
        assertThrows(NullPointerException.class, () -> daoIngrediente.pesquisaPorNome(null));
    }

    @Test
    public void deveDetectarMutacaoSQLInjection() throws Exception {
        // Teste para detectar vulnerabilidade de SQL injection
        when(ingrediente.getNome()).thenReturn("'; DROP TABLE tb_ingredientes; --");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        // Este teste deve falhar se a mutação remove a vulnerabilidade de SQL injection
        Ingrediente resultado = daoIngrediente.pesquisaPorNome(ingrediente);
        
        // Verificar se a query foi construída corretamente (deve conter a string maliciosa)
        verify(mockConnection).prepareStatement(contains("'; DROP TABLE tb_ingredientes; --"));
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    public void deveDetectarMutacaoPesquisaPorNomeComNomeVazio() throws Exception {
        when(ingrediente.getNome()).thenReturn("");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        Ingrediente resultado = daoIngrediente.pesquisaPorNome(ingrediente);
        assertNotNull(resultado);
        verify(mockConnection).prepareStatement(contains("WHERE nm_ingrediente=''"));
    }

    @Test
    public void deveDetectarMutacaoPesquisaPorNomeComCaracteresEspeciais() throws Exception {
        when(ingrediente.getNome()).thenReturn("Queijo' OR '1'='1");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        Ingrediente resultado = daoIngrediente.pesquisaPorNome(ingrediente);
        assertNotNull(resultado);
        verify(mockConnection).prepareStatement(contains("Queijo' OR '1'='1"));
    }
}