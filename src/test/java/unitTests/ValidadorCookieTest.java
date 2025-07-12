package unitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import DAO.DaoToken;
import Helpers.ValidadorCookie;
import jakarta.servlet.http.Cookie;

@DisplayName("Testes para ValidadorCookie")
public class ValidadorCookieTest {

    @Mock
    private DaoToken mockDaoToken; 

    @InjectMocks
    private ValidadorCookie validadorCookie; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Método: validarFuncionario")
    class ValidarFuncionarioTest {

        @Test
        @DisplayName("Deve retornar true se um cookie de funcionário válido for encontrado")
        void deveRetornarTrueParaCookieFuncionarioValido() {
            // Arrange
            Cookie[] cookies = {
                new Cookie("outroCookie", "qualquerValor"),
                new Cookie("tokenFuncionario", "token-valido-123")
            };
            when(mockDaoToken.validar("token-valido-123")).thenReturn(true);

            // Act
            boolean resultado = validadorCookie.validarFuncionario(cookies);

            // Assert
            assertTrue(resultado, "Deveria retornar true para um token de funcionário válido.");
            verify(mockDaoToken).validar("token-valido-123"); 
        }

        @Test
        @DisplayName("Deve retornar false se o token do funcionário for inválido")
        void deveRetornarFalseParaCookieFuncionarioInvalido() {
            // Arrange
            Cookie[] cookies = { new Cookie("tokenFuncionario", "token-invalido") };
            when(mockDaoToken.validar("token-invalido")).thenReturn(false);

            // Act
            boolean resultado = validadorCookie.validarFuncionario(cookies);

            // Assert
            assertFalse(resultado, "Deveria retornar false para um token de funcionário inválido.");
        }

        @Test
        @DisplayName("Deve retornar false se o array de cookies for nulo")
        void deveRetornarFalseParaArrayDeCookiesNulo() {
            // Act
            boolean resultado = validadorCookie.validarFuncionario(null);

            // Assert
            assertFalse(resultado, "Deveria retornar false quando o array de cookies é nulo.");
            verify(mockDaoToken, never()).validar(anyString()); 
        }

        @Test
        @DisplayName("Deve retornar false se nenhum cookie 'tokenFuncionario' for encontrado")
        void deveRetornarFalseSeCookieNaoEncontrado() {
            // Arrange
            Cookie[] cookies = { new Cookie("outroCookie", "valor") };

            // Act
            boolean resultado = validadorCookie.validarFuncionario(cookies);

            // Assert
            assertFalse(resultado, "Deveria retornar false se o cookie específico não for encontrado.");
        }
    }

    @Nested
    @DisplayName("Método: getCookieIdFuncionario")
    class GetCookieIdFuncionarioTest {

        @Test
        @DisplayName("Deve retornar o ID correto de um cookie de funcionário")
        void deveRetornarIdDoCookieDeFuncionario() {
            // Arrange
            Cookie[] cookies = { new Cookie("tokenFuncionario", "func-456-timestamp") };

            // Act
            String id = validadorCookie.getCookieIdFuncionario(cookies);

            // Assert
            assertEquals("func", id, "Deveria extrair e retornar a primeira parte do valor do cookie.");
        }
    }
    
    @Nested
    @DisplayName("Método: deletar")
    class DeletarTest {
        
        @Test
        @DisplayName("Deve chamar o método remover do DAO para cookies de token e tokenFuncionario")
        void deveChamarRemoverParaCookiesDeToken() {
            // Arrange
            Cookie[] cookies = {
                new Cookie("token", "token-cliente-abc"),
                new Cookie("tokenFuncionario", "token-func-def"),
                new Cookie("cookie-aleatorio", "ignorar")
            };

            // Act
            validadorCookie.deletar(cookies);

            // Assert
            verify(mockDaoToken, times(1)).remover("token-cliente-abc");
            verify(mockDaoToken, times(1)).remover("token-func-def");
            verify(mockDaoToken, never()).remover("ignorar");
        }

        @Test
        @DisplayName("Não deve fazer nada se o array de cookies for nulo")
        void naoDeveFazerNadaSeArrayNulo() {
            // Act
            validadorCookie.deletar(null);

            // Assert
            verifyNoInteractions(mockDaoToken);
        }
    }
}
