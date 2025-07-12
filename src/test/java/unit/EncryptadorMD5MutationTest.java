package unit;

import Helpers.EncryptadorMD5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("mutation")
public class EncryptadorMD5MutationTest {
    
    private EncryptadorMD5 encryptador = new EncryptadorMD5();
    
    @Test
    public void deveDetectarMutacaoReset() {
        // Teste para detectar mutação no reset()
        String resultado1 = encryptador.encryptar("teste");
        String resultado2 = encryptador.encryptar("teste");
        
        // Se o reset não for chamado, os resultados podem ser diferentes
        assertEquals(resultado1, resultado2, "Resultados devem ser iguais para mesma entrada");
    }
    
    @Test
    public void deveDetectarMutacaoUpdate() {
        // Teste para detectar mutação no update()
        String resultado1 = encryptador.encryptar("teste1");
        String resultado2 = encryptador.encryptar("teste2");
        
        // Resultados devem ser diferentes para entradas diferentes
        assertNotEquals(resultado1, resultado2, "Resultados devem ser diferentes para entradas diferentes");
    }
    
    @Test
    public void deveDetectarMutacaoDigest() {
        // Teste para detectar mutação no digest()
        String resultado = encryptador.encryptar("teste");
        
        // MD5 sempre retorna 32 caracteres hexadecimais
        assertEquals(32, resultado.length(), "MD5 deve retornar 32 caracteres");
        assertTrue(resultado.matches("[0-9a-f]{32}"), "MD5 deve conter apenas caracteres hexadecimais");
    }
    
    @Test
    public void deveDetectarMutacaoRetorno() {
        // Teste para detectar mutação no retorno
        String resultado = encryptador.encryptar("teste");
        
        // Resultado não deve ser null
        assertNotNull(resultado, "Resultado não deve ser null");
        
        // Resultado não deve estar vazio
        assertFalse(resultado.isEmpty(), "Resultado não deve estar vazio");
    }
    
    @Test
    public void deveDetectarMutacaoCondicional() {
        // Teste para detectar mutação em condicionais
        String resultado1 = encryptador.encryptar("a");
        String resultado2 = encryptador.encryptar("b");
        
        // Resultados devem ser diferentes
        assertNotEquals(resultado1, resultado2, "Entradas diferentes devem gerar hashes diferentes");
    }
    
    @Test
    public void deveDetectarMutacaoStringVazia() {
        // Teste para detectar mutação com string vazia
        String resultado = encryptador.encryptar("");
        
        // MD5 de string vazia é conhecido
        String md5Vazio = "d41d8cd98f00b204e9800998ecf8427e";
        assertEquals(md5Vazio, resultado, "MD5 de string vazia deve ser conhecido");
    }
    
    @Test
    public void deveDetectarMutacaoCaracteresEspeciais() {
        // Teste para detectar mutação com caracteres especiais
        String resultado = encryptador.encryptar("@#$%");
        
        assertNotNull(resultado, "Resultado não deve ser null");
        assertEquals(32, resultado.length(), "MD5 deve retornar 32 caracteres");
    }
} 