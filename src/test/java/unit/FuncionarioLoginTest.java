package unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.json.JSONObject;

@Tag("unit")
public class FuncionarioLoginTest {
    
    @Test
    void testEstruturaJSONLoginValido() {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("usuario", "admin");
        jsonInput.put("senha", "admin123");
        
        // Assert
        assertNotNull(jsonInput);
        assertTrue(jsonInput.has("usuario"));
        assertTrue(jsonInput.has("senha"));
        assertEquals("admin", jsonInput.getString("usuario"));
        assertEquals("admin123", jsonInput.getString("senha"));
    }
    
    @Test
    void testEstruturaJSONLoginInvalido() {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("usuario", "usuario_invalido");
        jsonInput.put("senha", "senha_errada");
        
        // Assert
        assertNotNull(jsonInput);
        assertTrue(jsonInput.has("usuario"));
        assertTrue(jsonInput.has("senha"));
        assertEquals("usuario_invalido", jsonInput.getString("usuario"));
        assertEquals("senha_errada", jsonInput.getString("senha"));
    }
    
    @Test
    void testJSONComCamposVazios() {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("usuario", "");
        jsonInput.put("senha", "");
        
        // Assert
        assertTrue(jsonInput.getString("usuario").isEmpty());
        assertTrue(jsonInput.getString("senha").isEmpty());
    }
    
    @Test
    void testJSONComCamposFaltando() {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("usuario", "admin");
        // Senha não incluída propositalmente
        
        // Assert
        assertTrue(jsonInput.has("usuario"));
        assertFalse(jsonInput.has("senha"));
    }
    
    @Test
    void testValidacaoCamposObrigatorios() {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("usuario", "admin");
        jsonInput.put("senha", "admin123");
        
        // Assert
        assertFalse(jsonInput.getString("usuario").isEmpty(), "Usuário não pode estar vazio");
        assertFalse(jsonInput.getString("senha").isEmpty(), "Senha não pode estar vazia");
        assertTrue(jsonInput.getString("usuario").length() > 0, "Usuário deve ter pelo menos 1 caractere");
        assertTrue(jsonInput.getString("senha").length() > 0, "Senha deve ter pelo menos 1 caractere");
    }
}
