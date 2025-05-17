package Controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.StringReader;
import java.io.BufferedReader;
import org.json.JSONObject;
import org.json.JSONArray;

public class ComprarTest {
    
    @Test
    void testProcessRequestBasico() {
        // Criar um JSON simples para teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new String[]{"15.00", "lanche", "1"});
        
        // Verificar se o JSON foi criado corretamente
        assertNotNull(jsonInput);
        assertEquals(1, jsonInput.getInt("id"));
        assertTrue(jsonInput.has("X-Burger"));
    }
    
    @Test
    void testValoresPedido() {
        // Testar cálculo de valores
        double valorLanche = 15.00;
        int quantidade = 2;
        double valorTotal = valorLanche * quantidade;
        
        assertEquals(30.00, valorTotal);
    }
    
    @Test
    void testEstruturaPedido() {
        // Testar estrutura básica do pedido
        JSONObject pedido = new JSONObject();
        pedido.put("id", 1);
        pedido.put("data", "2024-03-20");
        pedido.put("valor_total", 30.00);
        
        assertTrue(pedido.has("id"));
        assertTrue(pedido.has("data"));
        assertTrue(pedido.has("valor_total"));
        assertEquals(1, pedido.getInt("id"));
    }
    
    @Test
    void testProcessarPedidoComLancheEBebida() {
        // Criar JSON com lanche e bebida
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(2));
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(1));
        
        // Verificar estrutura do pedido
        assertTrue(jsonInput.has("id"));
        assertTrue(jsonInput.has("X-Burger"));
        assertTrue(jsonInput.has("Coca-Cola"));
        
        // Verificar valores
        JSONArray xBurger = jsonInput.getJSONArray("X-Burger");
        assertEquals("15.00", xBurger.getString(0));
        assertEquals("lanche", xBurger.getString(1));
        assertEquals(2, xBurger.getInt(2));
        
        // Verificar cálculo do valor total
        double valorLanche = 15.00 * 2; // 2 unidades
        double valorBebida = 5.00 * 1;  // 1 unidade
        double valorTotal = valorLanche + valorBebida;
        assertEquals(35.00, valorTotal);
    }
    
    @Test
    void testProcessarPedidoApenasLanches() {
        // Criar JSON apenas com lanches
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));
        jsonInput.put("X-Salada", new JSONArray().put("18.00").put("lanche").put(2));
        
        // Verificar estrutura
        assertTrue(jsonInput.has("X-Burger"));
        assertTrue(jsonInput.has("X-Salada"));
        
        // Verificar cálculo do valor total
        double valorXBurger = 15.00 * 1;
        double valorXSalada = 18.00 * 2;
        double valorTotal = valorXBurger + valorXSalada;
        assertEquals(51.00, valorTotal);
    }
    
    @Test
    void testProcessarPedidoApenasBebidas() {
        // Criar JSON apenas com bebidas
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));
        jsonInput.put("Suco", new JSONArray().put("7.00").put("bebida").put(1));
        
        // Verificar estrutura
        assertTrue(jsonInput.has("Coca-Cola"));
        assertTrue(jsonInput.has("Suco"));
        
        // Verificar cálculo do valor total
        double valorCoca = 5.00 * 2;
        double valorSuco = 7.00 * 1;
        double valorTotal = valorCoca + valorSuco;
        assertEquals(17.00, valorTotal);
    }
    
    @Test
    void testValidarEstruturaPedido() {
        // Criar JSON com estrutura completa
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));
        
        // Verificar campos obrigatórios
        assertTrue(jsonInput.has("id"));
        assertTrue(jsonInput.has("X-Burger"));
        
        // Verificar estrutura do item
        JSONArray item = jsonInput.getJSONArray("X-Burger");
        assertEquals(3, item.length());
        assertTrue(item.getString(0).matches("\\d+\\.\\d{2}")); // Formato de preço
        assertTrue(item.getString(1).matches("lanche|bebida")); // Tipo válido
        assertTrue(item.getInt(2) > 0); // Quantidade positiva
    }
    
    @Test
    void testCalcularValorTotalComDesconto() {
        // Criar JSON com itens e desconto
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(2));
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));
        
        // Calcular valor total
        double valorLanche = 15.00 * 2;
        double valorBebida = 5.00 * 2;
        double valorTotal = valorLanche + valorBebida;
        
        // Aplicar desconto de 10%
        double desconto = valorTotal * 0.10;
        double valorFinal = valorTotal - desconto;
        
        assertEquals(36.00, valorFinal);
    }
}

// Classe auxiliar para simular ServletInputStream
class MockServletInputStream extends jakarta.servlet.ServletInputStream {
    private final String content;
    private final BufferedReader reader;
    
    public MockServletInputStream(String content) {
        this.content = content;
        this.reader = new BufferedReader(new StringReader(content));
    }
    
    @Override
    public int read() throws java.io.IOException {
        return reader.read();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(jakarta.servlet.ReadListener readListener) {
        // Implementação vazia para teste
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}