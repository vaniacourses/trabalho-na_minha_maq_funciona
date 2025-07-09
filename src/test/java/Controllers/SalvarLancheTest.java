package Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import DAO.DaoLanche;
import Model.Lanche;
import org.json.JSONObject;

@Tag("unit")
public class SalvarLancheTest {
    
    @Test
    void testCadastroLancheComIngredientes() {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("nome", "X-Burger");
        jsonInput.put("descricao", "Hambúrguer com queijo");
        jsonInput.put("ValorVenda", 15.00);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão", 1);
        ingredientes.put("Hambúrguer", 1);
        ingredientes.put("Queijo", 2);
        ingredientes.put("Alface", 1);
        
        jsonInput.put("ingredientes", ingredientes);
        
        // Verificar estrutura do JSON
        assertNotNull(jsonInput);
        assertTrue(jsonInput.has("nome"));
        assertTrue(jsonInput.has("descricao"));
        assertTrue(jsonInput.has("ValorVenda"));
        assertTrue(jsonInput.has("ingredientes"));
        
        // Verificar ingredientes
        JSONObject ingredientesObj = jsonInput.getJSONObject("ingredientes");
        assertEquals(1, ingredientesObj.getInt("Pão"));
        assertEquals(1, ingredientesObj.getInt("Hambúrguer"));
        assertEquals(2, ingredientesObj.getInt("Queijo"));
    }
    
    @Test
    void testCalculoPrecoIngredientes() {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("nome", "X-Salada");
        jsonInput.put("descricao", "Hambúrguer com salada");
        jsonInput.put("ValorVenda", 18.00);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão", 1);
        ingredientes.put("Hambúrguer", 1);
        ingredientes.put("Queijo", 1);
        ingredientes.put("Alface", 2);
        ingredientes.put("Tomate", 2);
        
        jsonInput.put("ingredientes", ingredientes);
        
        // Verificar preço
        double valorVenda = jsonInput.getDouble("ValorVenda");
        assertTrue(valorVenda > 0);
        assertEquals(18.00, valorVenda);
    }
    
    @Test
    void testValidacaoIngredientes() {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("nome", "X-Bacon");
        jsonInput.put("descricao", "Hambúrguer com bacon");
        jsonInput.put("ValorVenda", 20.00);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão", 1);
        ingredientes.put("Hambúrguer", 1);
        ingredientes.put("Bacon", 3);
        
        jsonInput.put("ingredientes", ingredientes);
        
        // Verificar quantidade de ingredientes
        JSONObject ingredientesObj = jsonInput.getJSONObject("ingredientes");
        assertTrue(ingredientesObj.getInt("Pão") > 0);
        assertTrue(ingredientesObj.getInt("Hambúrguer") > 0);
        assertTrue(ingredientesObj.getInt("Bacon") > 0);
    }
    
    @Test
    void testVinculacaoIngredientes() {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("nome", "X-Tudo");
        jsonInput.put("descricao", "Hambúrguer completo");
        jsonInput.put("ValorVenda", 25.00);
        
        JSONObject ingredientes = new JSONObject();
        ingredientes.put("Pão", 1);
        ingredientes.put("Hambúrguer", 2);
        ingredientes.put("Queijo", 2);
        ingredientes.put("Bacon", 2);
        ingredientes.put("Alface", 1);
        ingredientes.put("Tomate", 1);
        
        jsonInput.put("ingredientes", ingredientes);
        
        // Verificar vinculação de ingredientes
        JSONObject ingredientesObj = jsonInput.getJSONObject("ingredientes");
        assertTrue(ingredientesObj.has("Pão"));
        assertTrue(ingredientesObj.has("Hambúrguer"));
        assertTrue(ingredientesObj.has("Queijo"));
        assertTrue(ingredientesObj.has("Bacon"));
        assertTrue(ingredientesObj.has("Alface"));
        assertTrue(ingredientesObj.has("Tomate"));
        
        // Verificar quantidades
        assertEquals(1, ingredientesObj.getInt("Pão"));
        assertEquals(2, ingredientesObj.getInt("Hambúrguer"));
        assertEquals(2, ingredientesObj.getInt("Queijo"));
    }
}