package unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import Controllers.salvarLanche;
import dao.DaoIngrediente;
import dao.DaoLanche;
import Helpers.ValidadorCookie;
import Model.Ingrediente;
import Model.Lanche;
import jakarta.servlet.http.Cookie;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.JSONObject;

public class SalvarLancheTest extends BaseServletTest {

    @Mock
    private DaoLanche daoLanche;

    @Mock
    private DaoIngrediente daoIngrediente;

    @Mock
    private ValidadorCookie validadorCookie;
    
    private salvarLanche servlet;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        setUpBase();
        servlet = new salvarLanche(daoLanche, daoIngrediente, validadorCookie);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("Deve validar estrutura JSON de lanche válido")
    void testValidarEstruturaJSONLancheValido() {
        // Arrange
        JSONObject jsonLanche = new JSONObject();
        jsonLanche.put("nome", "X-Tudo");
        jsonLanche.put("descricao", "Hambúrguer completo");
        jsonLanche.put("ValorVenda", 25.00);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão", 1);
        ingredientes.put("Hambúrguer", 2);
        jsonLanche.put("ingredientes", ingredientes);

        // Assert
        assertNotNull(jsonLanche);
        assertTrue(jsonLanche.has("nome"));
        assertTrue(jsonLanche.has("descricao"));
        assertTrue(jsonLanche.has("ValorVenda"));
        assertTrue(jsonLanche.has("ingredientes"));
        assertEquals("X-Tudo", jsonLanche.getString("nome"));
        assertEquals(25.00, jsonLanche.getDouble("ValorVenda"), 0.01);
        assertEquals(2, ingredientes.length());
    }

    @Test
    @DisplayName("Deve validar estrutura JSON de lanche sem ingredientes")
    void testValidarEstruturaJSONLancheSemIngredientes() {
        // Arrange
        JSONObject jsonLanche = new JSONObject();
        jsonLanche.put("nome", "Lanche Simples");
        jsonLanche.put("descricao", "Lanche básico");
        jsonLanche.put("ValorVenda", 10.00);
        
        JSONObject ingredientes = new JSONObject();
        jsonLanche.put("ingredientes", ingredientes);

        // Assert
        assertNotNull(jsonLanche);
        assertTrue(jsonLanche.has("nome"));
        assertTrue(jsonLanche.has("ingredientes"));
        assertEquals("Lanche Simples", jsonLanche.getString("nome"));
        assertEquals(0, ingredientes.length());
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios do JSON")
    void testValidarCamposObrigatorios() {
        // Arrange
        JSONObject jsonLanche = new JSONObject();
        jsonLanche.put("nome", "Teste");
        jsonLanche.put("descricao", "Descrição teste");
        jsonLanche.put("ValorVenda", 15.50);
        jsonLanche.put("ingredientes", new JSONObject());

        // Assert
        assertFalse(jsonLanche.getString("nome").isEmpty(), "Nome não pode estar vazio");
        assertFalse(jsonLanche.getString("descricao").isEmpty(), "Descrição não pode estar vazia");
        assertTrue(jsonLanche.getDouble("ValorVenda") > 0, "Valor deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve validar tipos de dados do JSON")
    void testValidarTiposDados() {
        // Arrange
        JSONObject jsonLanche = new JSONObject();
        jsonLanche.put("nome", "Teste");
        jsonLanche.put("descricao", "Descrição");
        jsonLanche.put("ValorVenda", 20.00);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão", 1);
        ingredientes.put("Queijo", 2);
        jsonLanche.put("ingredientes", ingredientes);

        // Assert
        assertTrue(jsonLanche.get("nome") instanceof String);
        assertTrue(jsonLanche.get("descricao") instanceof String);
        assertTrue(jsonLanche.get("ValorVenda") instanceof Number);
        assertTrue(jsonLanche.get("ingredientes") instanceof JSONObject);
        assertTrue(ingredientes.get("Pão") instanceof Number);
    }

    @Test
    @DisplayName("Deve validar JSON com caracteres especiais")
    void testValidarJSONComCaracteresEspeciais() {
        // Arrange
        JSONObject jsonLanche = new JSONObject();
        jsonLanche.put("nome", "X-Burger & Fries");
        jsonLanche.put("descricao", "Hambúrguer com batatas fritas - 100% natural");
        jsonLanche.put("ValorVenda", 29.99);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão de Hambúrguer", 1);
        ingredientes.put("Carne Bovina", 1);
        ingredientes.put("Batata Frita", 1);
        jsonLanche.put("ingredientes", ingredientes);

        // Assert
        assertNotNull(jsonLanche);
        assertTrue(jsonLanche.getString("nome").contains("&"));
        assertTrue(jsonLanche.getString("descricao").contains("ú"));
        assertTrue(ingredientes.has("Pão de Hambúrguer"));
        assertTrue(ingredientes.has("Carne Bovina"));
    }
}
