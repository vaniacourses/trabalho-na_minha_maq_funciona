package unit;

import Helpers.ValidadorCookie;
import dao.DaoToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("mutation")
public class ValidadorCookieMutationTest {
    
    @Mock
    private DaoToken mockDaoToken;
    
    private ValidadorCookie validador;
    
    @Test
    public void deveDetectarMutacaoValidarCookiesNull() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        
        // Act
        boolean resultado = validador.validar(null);
        
        // Assert
        assertFalse(resultado, "Cookies null deve ser inválido");
    }
    
    @Test
    public void deveDetectarMutacaoValidarCookiesVazio() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie[] cookies = new Cookie[0];
        
        // Act
        boolean resultado = validador.validar(cookies);
        
        // Assert
        assertFalse(resultado, "Array de cookies vazio deve ser inválido");
    }
    
    @Test
    public void deveDetectarMutacaoValidarTokenCliente() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("token", "valid_token");
        Cookie[] cookies = {cookie};
        
        when(mockDaoToken.validar("valid_token")).thenReturn(true);
        
        // Act
        boolean resultado = validador.validar(cookies);
        
        // Assert
        assertTrue(resultado, "Token válido deve retornar true");
        verify(mockDaoToken).validar("valid_token");
    }
    
    @Test
    public void deveDetectarMutacaoValidarTokenFuncionario() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("tokenFuncionario", "valid_token");
        Cookie[] cookies = {cookie};
        
        when(mockDaoToken.validar("valid_token")).thenReturn(true);
        
        // Act
        boolean resultado = validador.validarFuncionario(cookies);
        
        // Assert
        assertTrue(resultado, "Token de funcionário válido deve retornar true");
        verify(mockDaoToken).validar("valid_token");
    }
    
    @Test
    public void deveDetectarMutacaoValidarTokenInvalido() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("token", "invalid_token");
        Cookie[] cookies = {cookie};
        
        when(mockDaoToken.validar("invalid_token")).thenReturn(false);
        
        // Act
        boolean resultado = validador.validar(cookies);
        
        // Assert
        assertFalse(resultado, "Token inválido deve retornar false");
        verify(mockDaoToken).validar("invalid_token");
    }
    
    @Test
    public void deveDetectarMutacaoValidarNomeCookieIncorreto() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("outro", "valid_token");
        Cookie[] cookies = {cookie};
        
        // Act
        boolean resultado = validador.validar(cookies);
        
        // Assert
        assertFalse(resultado, "Cookie com nome incorreto deve retornar false");
        verify(mockDaoToken, never()).validar(anyString());
    }
    
    @Test
    public void deveDetectarMutacaoGetCookieIdCliente() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("token", "123-abc-def");
        Cookie[] cookies = {cookie};
        
        // Act
        String resultado = validador.getCookieIdCliente(cookies);
        
        // Assert
        assertEquals("123", resultado, "Deve extrair o ID do cliente corretamente");
    }
    
    @Test
    public void deveDetectarMutacaoGetCookieIdFuncionario() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("tokenFuncionario", "456-xyz-uvw");
        Cookie[] cookies = {cookie};
        
        // Act
        String resultado = validador.getCookieIdFuncionario(cookies);
        
        // Assert
        assertEquals("456", resultado, "Deve extrair o ID do funcionário corretamente");
    }
    
    @Test
    public void deveDetectarMutacaoGetCookieIdClienteSemToken() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("outro", "123-abc-def");
        Cookie[] cookies = {cookie};
        
        // Act
        String resultado = validador.getCookieIdCliente(cookies);
        
        // Assert
        assertEquals("erro", resultado, "Deve retornar erro quando não encontra token");
    }
    
    @Test
    public void deveDetectarMutacaoGetCookieIdFuncionarioSemToken() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        validador = new ValidadorCookie(mockDaoToken);
        Cookie cookie = new Cookie("outro", "456-xyz-uvw");
        Cookie[] cookies = {cookie};
        
        // Act
        String resultado = validador.getCookieIdFuncionario(cookies);
        
        // Assert
        assertEquals("erro", resultado, "Deve retornar erro quando não encontra token");
    }
} 