package unitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Classe base para testes de servlets com configurações comuns
 */
public abstract class BaseServletTest {
    
    @Mock
    protected HttpServletRequest request;
    
    @Mock
    protected HttpServletResponse response;
    
    protected StringWriter stringWriter;
    protected PrintWriter writer;
    
    @BeforeEach
    void setUpBase() throws Exception {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }
    
    /**
     * Utilitário para criar cookies de teste
     */
    protected Cookie[] createTestCookies(String name, String value) {
        return new Cookie[]{new Cookie(name, value)};
    }
    
    /**
     * Utilitário para criar cookies de token válido
     */
    protected Cookie[] createValidTokenCookies() {
        return createTestCookies("token", "valid_token");
    }
    
    /**
     * Utilitário para criar cookies de funcionário válido
     */
    protected Cookie[] createValidFuncionarioCookies() {
        return createTestCookies("id_funcionario", "1");
    }
    
    /**
     * Utilitário para verificar se a resposta contém sucesso
     */
    protected void assertSuccessResponse() {
        String response = stringWriter.toString();
        assertTrue(response.contains("Sucesso") || response.contains("Cadastrado") || 
                  response.contains("Salvo"), "Resposta deveria indicar sucesso: " + response);
    }
    
    /**
     * Utilitário para verificar se a resposta contém erro
     */
    protected void assertErrorResponse() {
        String response = stringWriter.toString();
        assertTrue(response.contains("erro") || response.contains("Erro") || 
                  response.contains("inválido"), "Resposta deveria indicar erro: " + response);
    }
    
    /**
     * MockServletInputStream reutilizável
     */
    protected static class MockServletInputStream extends jakarta.servlet.ServletInputStream {
        private final java.io.ByteArrayInputStream bais;
        
        public MockServletInputStream(String content) {
            this.bais = new java.io.ByteArrayInputStream(content.getBytes());
        }
        
        @Override
        public int read() throws java.io.IOException {
            return bais.read();
        }
        
        @Override
        public boolean isFinished() {
            return bais.available() == 0;
        }
        
        @Override
        public boolean isReady() {
            return true;
        }
        
        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener) {}
    }
} 