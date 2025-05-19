package Controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import Helpers.ValidadorCookie;
import DAO.DaoFuncionario;
import Model.Funcionario;

public class SalvarFuncionarioTest {
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private DaoFuncionario daoFuncionario;
    
    @Mock
    private ValidadorCookie validadorCookie;
    
    private salvarFuncionario servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet = new salvarFuncionario(daoFuncionario, validadorCookie);
    }
    
    @Test
    void testProcessRequestComCookieValido() throws Exception {
        // Arrange
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("id_funcionario", "1");
        
        String jsonInput = "{\"nome\":\"João\",\"salario\":3000.00,\"usuarioFuncionario\":\"joao123\",\"senhaFuncionario\":\"senha123\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario).salvar(any(Funcionario.class));
        assertTrue(stringWriter.toString().contains("Funcionario Cadastrado!"));
    }
    
    @Test
    void testProcessRequestSemCookie() throws Exception {
        // Arrange
        when(request.getCookies()).thenReturn(null);
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertEquals("erro", stringWriter.toString().trim());
    }
    
    @Test
    void testProcessRequestComDadosInvalidos() throws Exception {
        // Arrange
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("id_funcionario", "1");
        
        String jsonInput = "{\"nome\":\"\",\"salario\":-1000.00,\"usuarioFuncionario\":\"\",\"senhaFuncionario\":\"\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }
    
    @Test
    void testProcessRequestComSalarioInvalido() throws Exception {
        // Arrange
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("id_funcionario", "1");
        
        String jsonInput = "{\"nome\":\"Maria\",\"salario\":0.00,\"usuarioFuncionario\":\"maria123\",\"senhaFuncionario\":\"senha123\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }
}

class MockServletInputStream extends ServletInputStream {
    private final StringReader reader;
    
    public MockServletInputStream(String content) {
        this.reader = new StringReader(content);
    }
    
    @Override
    public int read() throws IOException {
        return reader.read();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
    public boolean isReady() {
        return true;
    }
    
    @Override
    public void setReadListener(jakarta.servlet.ReadListener readListener) {
        // Não é necessário implementar para testes
    }
}

