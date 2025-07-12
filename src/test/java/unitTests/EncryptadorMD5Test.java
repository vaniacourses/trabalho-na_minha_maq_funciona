package unitTests;

import Helpers.EncryptadorMD5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptadorMD5Test {
    
    private EncryptadorMD5 encryptador;
    
    @BeforeEach
    void setUp() {
        encryptador = new EncryptadorMD5();
    }
    
    @Test
    void testEncryptarSenhaValida() {
        // Teste com senha válida
        String senha = "123456";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres (MD5 padrão)
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido (apenas caracteres hexadecimais)
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaVazia() {
        // Teste com senha vazia
        String senha = "";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComCaracteresEspeciais() {
        // Teste com senha com caracteres especiais
        String senha = "senha@123#";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaLonga() {
        // Teste com senha longa
        String senha = "esta_e_uma_senha_muito_longa_com_muitos_caracteres_para_testar_o_comportamento_do_encryptador";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComEspacos() {
        // Teste com senha com espaços
        String senha = "senha com espaços";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComNumeros() {
        // Teste com senha apenas números
        String senha = "123456789";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComLetras() {
        // Teste com senha apenas letras
        String senha = "abcdefghijklmnop";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComCaracteresUnicode() {
        // Teste com senha com caracteres unicode
        String senha = "senha_com_ç_ã_é_ñ";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComUmCaractere() {
        // Teste com senha de um caractere
        String senha = "a";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaComZeroPadding() {
        // Teste para verificar se o zero padding funciona corretamente
        // Algumas senhas podem gerar hashes que precisam de padding
        String senha = "test";
        String resultado = encryptador.encryptar(senha);
        
        // Verificar que não é nulo e tem 32 caracteres
        assertNotNull(resultado);
        assertEquals(32, resultado.length());
        
        // Verificar que é um hash MD5 válido
        assertTrue(resultado.matches("^[a-f0-9]{32}$"));
    }
    
    @Test
    void testEncryptarSenhaNull() {
        // Teste com senha nula
        String resultado = encryptador.encryptar(null);
        
        // Deve retornar null quando a entrada é null
        assertNull(resultado);
    }
    
    @Test
    void testConsistenciaEncryptacao() {
        // Teste para verificar se a mesma senha sempre gera o mesmo hash
        String senha = "senha_teste";
        String resultado1 = encryptador.encryptar(senha);
        String resultado2 = encryptador.encryptar(senha);
        
        // Verificar que os resultados são iguais
        assertEquals(resultado1, resultado2);
        
        // Verificar que ambos são válidos
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertEquals(32, resultado1.length());
        assertEquals(32, resultado2.length());
    }
    
    @Test
    void testDiferentesSenhasGeramDiferentesHashes() {
        // Teste para verificar se senhas diferentes geram hashes diferentes
        String senha1 = "senha1";
        String senha2 = "senha2";
        
        String resultado1 = encryptador.encryptar(senha1);
        String resultado2 = encryptador.encryptar(senha2);
        
        // Verificar que os resultados são diferentes
        assertNotEquals(resultado1, resultado2);
        
        // Verificar que ambos são válidos
        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertEquals(32, resultado1.length());
        assertEquals(32, resultado2.length());
    }
} 