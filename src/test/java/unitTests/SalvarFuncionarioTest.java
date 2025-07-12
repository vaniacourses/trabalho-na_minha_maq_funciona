package unitTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.Cookie;
import Helpers.ValidadorCookie;
import Model.Funcionario;
import Controllers.salvarFuncionario;
import dao.DaoFuncionario;
import dao.DaoToken;

@Tag("unit")
public class SalvarFuncionarioTest extends BaseServletTest {
    
    @Mock
    private DaoFuncionario daoFuncionario;
    
    @Mock
    private ValidadorCookie validadorCookie;
    
    private salvarFuncionario servlet;
    
    @BeforeEach
    void setUp() throws Exception {
        setUpBase();
        servlet = new salvarFuncionario(daoFuncionario, validadorCookie);
    }
    
    @Test
    void testCadastroFuncionarioSucesso() throws Exception {
        // Arrange
        Cookie[] cookies = createValidFuncionarioCookies();
        String jsonInput = "{\"nome\":\"João Silva\",\"salario\":3000.00,\"usuarioFuncionario\":\"joao123\",\"senhaFuncionario\":\"senha123\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario).salvar(any(Funcionario.class));
        assertSuccessResponse();
    }
    
    @Test
    void testValidacaoPermissoesSemAcesso() throws Exception {
        // Arrange
        Cookie[] cookies = createTestCookies("id_funcionario", "2"); // ID sem permissão
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(false);
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertErrorResponse();
    }
    
    @Test
    void testValidacaoDadosObrigatorios() throws Exception {
        // Arrange
        Cookie[] cookies = createValidFuncionarioCookies();
        String jsonInput = "{\"nome\":\"\",\"salario\":0.00,\"usuarioFuncionario\":\"\",\"senhaFuncionario\":\"\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertErrorResponse();
    }
    
    @Test
    void testCadastroFuncionarioSemCookies() throws Exception {
        // Arrange
        when(request.getCookies()).thenReturn(null);
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertErrorResponse();
    }
    
    @Test
    void testCadastroFuncionarioJSONInvalido() throws Exception {
        // Arrange
        Cookie[] cookies = createValidFuncionarioCookies();
        String jsonInput = "json inválido";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            servlet.processRequest(request, response);
        });
        
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
    }
    
    @Test
    void testCadastroFuncionarioSalarioNegativo() throws Exception {
        // Arrange
        Cookie[] cookies = createValidFuncionarioCookies();
        String jsonInput = "{\"nome\":\"João\",\"salario\":-1000.00,\"usuarioFuncionario\":\"joao123\",\"senhaFuncionario\":\"senha123\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario, never()).salvar(any(Funcionario.class));
        assertErrorResponse();
    }
    
    @Test
    void testCadastroFuncionarioUsuarioDuplicado() throws Exception {
        // Arrange
        Cookie[] cookies = createValidFuncionarioCookies();
        String jsonInput = "{\"nome\":\"João\",\"salario\":3000.00,\"usuarioFuncionario\":\"usuario_existente\",\"senhaFuncionario\":\"senha123\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        doThrow(new RuntimeException("Usuário já existe")).when(daoFuncionario).salvar(any(Funcionario.class));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            servlet.processRequest(request, response);
        });
        
        verify(daoFuncionario).salvar(any(Funcionario.class));
    }
    
    @Test
    void testCadastroFuncionarioDadosCompletos() throws Exception {
        // Arrange
        Cookie[] cookies = createValidFuncionarioCookies();
        String jsonInput = "{\"nome\":\"Maria Santos\",\"salario\":4500.00,\"usuarioFuncionario\":\"maria.santos\",\"senhaFuncionario\":\"senha456\"}";
        
        when(request.getCookies()).thenReturn(cookies);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));
        when(validadorCookie.validarFuncionario(cookies)).thenReturn(true);
        when(validadorCookie.getCookieIdFuncionario(cookies)).thenReturn("1");
        
        // Act
        servlet.processRequest(request, response);
        
        // Assert
        verify(daoFuncionario).salvar(argThat(funcionario -> 
            funcionario.getNome().equals("Maria Santos") &&
            funcionario.getSalario() == 4500.00 &&
            funcionario.getUsuario().equals("maria.santos")
        ));
        assertSuccessResponse();
    }
}

