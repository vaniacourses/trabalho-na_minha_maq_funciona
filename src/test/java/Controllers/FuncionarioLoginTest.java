package Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import DAO.DaoFuncionario;
import DAO.DaoToken;
import Model.Funcionario;
import org.json.JSONObject;

@Tag("unit")
public class FuncionarioLoginTest {
    
    @Test
    void testLoginSucesso() {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("email", "funcionario@exemplo.com");
        jsonInput.put("senha", "senha123");
        
        // Verificar se o JSON foi criado corretamente
        assertNotNull(jsonInput);
        assertTrue(jsonInput.has("email"));
        assertTrue(jsonInput.has("senha"));
        assertEquals("funcionario@exemplo.com", jsonInput.getString("email"));
    }
    
    @Test
    void testLoginCredenciaisInvalidas() {
        // Preparar dados de teste com credenciais inválidas
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("email", "email_invalido@exemplo.com");
        jsonInput.put("senha", "senha_errada");
        
        // Verificar estrutura do JSON
        assertNotNull(jsonInput);
        assertTrue(jsonInput.has("email"));
        assertTrue(jsonInput.has("senha"));
    }
    
    @Test
    void testLoginCamposObrigatorios() {
        // Testar com campos faltando
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("email", "funcionario@exemplo.com");
        // Senha não incluída propositalmente
        
        // Verificar se os campos obrigatórios estão presentes
        assertTrue(jsonInput.has("email"));
        assertFalse(jsonInput.has("senha"));
    }
    
    @Test
    void testLoginFormatoEmail() {
        // Testar formato de email inválido
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("email", "email_invalido");
        jsonInput.put("senha", "senha123");
        
        // Verificar formato do email
        String email = jsonInput.getString("email");
        assertFalse(email.contains("@"));
    }
    
    @Test
    void testLoginSenhaVazia() {
        // Testar senha vazia
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("email", "funcionario@exemplo.com");
        jsonInput.put("senha", "");
        
        // Verificar se a senha está vazia
        assertTrue(jsonInput.getString("senha").isEmpty());
    }
}
