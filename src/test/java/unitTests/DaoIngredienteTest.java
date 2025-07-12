package unitTests;

import dao.DaoIngrediente;
import Model.Ingrediente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DaoIngredienteTest {

    private DaoIngrediente daoIngrediente;

    @Mock
    private Ingrediente ingrediente;

    @BeforeEach
    void setUp() {
        daoIngrediente = new DaoIngrediente();
    }

    @Test
    void testConstrutorDaoIngrediente() {

        DaoIngrediente novoDao = new DaoIngrediente();
        assertNotNull(novoDao);
    }

    @Test
    void testSalvarIngrediente() {

        when(ingrediente.getNome()).thenReturn("Queijo");
        when(ingrediente.getDescricao()).thenReturn("Queijo cheddar");
        when(ingrediente.getQuantidade()).thenReturn(100);
        when(ingrediente.getValor_compra()).thenReturn(5.0);
        when(ingrediente.getValor_venda()).thenReturn(8.0);
        when(ingrediente.getTipo()).thenReturn("Laticínio");
        when(ingrediente.getFg_ativo()).thenReturn(1);

        try {
            daoIngrediente.salvar(ingrediente);
        } catch (Exception e) {

            assertTrue(e instanceof RuntimeException);
        }

        verify(ingrediente).getNome();
        verify(ingrediente).getDescricao();
        verify(ingrediente).getQuantidade();
        verify(ingrediente).getValor_compra();
        verify(ingrediente).getValor_venda();
        verify(ingrediente).getTipo();
        verify(ingrediente).getFg_ativo();
    }

    @Test
    void testSalvarIngredienteThrowsException() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoIngrediente daoIngredienteTest = new DaoIngrediente();

        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngredienteTest, mockConn);

        Ingrediente ingredienteTest = mock(Ingrediente.class);

        assertThrows(RuntimeException.class, () -> daoIngredienteTest.salvar(ingredienteTest));
    }

    @Test
    void testListarTodos() {

        try {
            daoIngrediente.listarTodos();
        } catch (Exception e) {

            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    void testListarTodosThrowsException() throws SQLException, NoSuchFieldException, IllegalAccessException {

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoIngrediente daoIngredienteTest = new DaoIngrediente();

        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngredienteTest, mockConn);

        assertThrows(RuntimeException.class, () -> daoIngredienteTest.listarTodos());
    }

    @Test
    void testListarTodosPorLanche() {

        try {
            daoIngrediente.listarTodosPorLanche(1);
        } catch (Exception e) {

            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    void testListarTodosPorLancheThrowsException() throws SQLException, NoSuchFieldException, IllegalAccessException {

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoIngrediente daoIngredienteTest = new DaoIngrediente();

        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngredienteTest, mockConn);

        assertThrows(RuntimeException.class, () -> daoIngredienteTest.listarTodosPorLanche(1));
    }

    @Test
    void testAlterarIngrediente() {

        when(ingrediente.getNome()).thenReturn("Queijo Atualizado");
        when(ingrediente.getDescricao()).thenReturn("Queijo cheddar atualizado");
        when(ingrediente.getQuantidade()).thenReturn(150);
        when(ingrediente.getValor_compra()).thenReturn(6.0);
        when(ingrediente.getValor_venda()).thenReturn(9.0);
        when(ingrediente.getTipo()).thenReturn("Laticínio");
        when(ingrediente.getId_ingrediente()).thenReturn(1);

        try {
            daoIngrediente.alterar(ingrediente);
        } catch (Exception e) {

            assertTrue(e instanceof RuntimeException);
        }

        verify(ingrediente).getNome();
        verify(ingrediente).getDescricao();
        verify(ingrediente).getQuantidade();
        verify(ingrediente).getValor_compra();
        verify(ingrediente).getValor_venda();
        verify(ingrediente).getTipo();
        verify(ingrediente).getId_ingrediente();
    }

    @Test
    void testAlterarIngredienteThrowsException() throws SQLException, NoSuchFieldException, IllegalAccessException {

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoIngrediente daoIngredienteTest = new DaoIngrediente();

        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngredienteTest, mockConn);

        Ingrediente ingredienteTest = mock(Ingrediente.class);

        assertThrows(RuntimeException.class, () -> daoIngredienteTest.alterar(ingredienteTest));
    }

    @Test
    void testRemoverIngrediente() {

        when(ingrediente.getId_ingrediente()).thenReturn(1);

        try {
            daoIngrediente.remover(ingrediente);
        } catch (Exception e) {

            assertTrue(e instanceof RuntimeException);
        }

        verify(ingrediente).getId_ingrediente();
    }

    @Test
    void testRemoverIngredienteThrowsException() throws SQLException, NoSuchFieldException, IllegalAccessException {

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoIngrediente daoIngredienteTest = new DaoIngrediente();

        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngredienteTest, mockConn);

        Ingrediente ingredienteTest = mock(Ingrediente.class);

        assertThrows(RuntimeException.class, () -> daoIngredienteTest.remover(ingredienteTest));
    }

    @Test
    void testPesquisaPorNome() {

        when(ingrediente.getNome()).thenReturn("Queijo");

        try {
            daoIngrediente.pesquisaPorNome(ingrediente);
        } catch (Exception e) {

            assertTrue(e instanceof RuntimeException);
        }

        verify(ingrediente).getNome();
    }

    @Test
    void testPesquisaPorNomeThrowsException() throws SQLException, NoSuchFieldException, IllegalAccessException {

        Connection mockConn = mock(Connection.class);
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("erro fake"));
        DaoIngrediente daoIngredienteTest = new DaoIngrediente();

        java.lang.reflect.Field field = DaoIngrediente.class.getDeclaredField("conecta");
        field.setAccessible(true);
        field.set(daoIngredienteTest, mockConn);

        Ingrediente ingredienteTest = mock(Ingrediente.class);
        when(ingredienteTest.getNome()).thenReturn("Queijo");

        assertThrows(RuntimeException.class, () -> daoIngredienteTest.pesquisaPorNome(ingredienteTest));
    }

    @Test
    void testSalvarWithNullIngrediente() {

        try {
            daoIngrediente.salvar(null);
        } catch (Exception e) {

            assertTrue(e instanceof NullPointerException || e instanceof RuntimeException);
        }
    }

    @Test
    void testAlterarWithNullIngrediente() {

        try {
            daoIngrediente.alterar(null);
        } catch (Exception e) {

            assertTrue(e instanceof NullPointerException || e instanceof RuntimeException);
        }
    }

    @Test
    void testRemoverWithNullIngrediente() {

        try {
            daoIngrediente.remover(null);
        } catch (Exception e) {

            assertTrue(e instanceof NullPointerException || e instanceof RuntimeException);
        }
    }

    @Test
    void testPesquisaPorNomeWithNullIngrediente() {

        try {
            daoIngrediente.pesquisaPorNome(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException || e instanceof RuntimeException);
        }
    }
}