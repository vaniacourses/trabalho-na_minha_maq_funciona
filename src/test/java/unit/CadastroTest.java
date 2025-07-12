package unit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import Model.Cliente;
import Model.Endereco;
import Controllers.cadastro;
import dao.DaoCliente;
import dao.DaoEndereco;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

@Tag("unit")
public class CadastroTest extends BaseServletTest {
    @Mock
    private DaoCliente daoCliente;

    private cadastro servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        setUpBase();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet = new cadastro(daoCliente);
    }

    @Test
    void testCadastroClienteSucesso() throws Exception {
        // Arrange
        String jsonInput = "{\"usuario\":{\"nome\":\"João\",\"sobrenome\":\"Silva\",\"telefone\":\"21987654321\",\"usuario\":\"joao123\",\"senha\":\"123456\"},"
                + "\"endereco\":{\"rua\":\"Rua das Flores\",\"bairro\":\"Centro\",\"numero\":123,\"complemento\":\"Apto 101\",\"cidade\":\"Rio de Janeiro\",\"estado\":\"RJ\"}}";
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoCliente).salvar(any(Cliente.class));
        writer.flush();
        assertTrue(stringWriter.toString().contains("Usuário Cadastrado!"));
    }

    @Test
    void testCadastroClienteComDadosMinimos() throws Exception {
        // Arrange
        String jsonInput = "{\"usuario\":{\"nome\":\"Maria\",\"sobrenome\":\"Santos\",\"telefone\":\"11987654321\",\"usuario\":\"maria\",\"senha\":\"senha123\"},"
                + "\"endereco\":{\"rua\":\"Rua A\",\"bairro\":\"Bairro B\",\"numero\":1,\"complemento\":\"\",\"cidade\":\"São Paulo\",\"estado\":\"SP\"}}";
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoCliente).salvar(any(Cliente.class));
        writer.flush();
        assertTrue(stringWriter.toString().contains("Usuário Cadastrado!"));
    }

    @Test
    void testCadastroClienteJSONInvalido() throws Exception {
        // Arrange
        String jsonInput = "json inválido";
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));

        // Act & Assert
        assertThrows(Exception.class, () -> {
            servlet.processRequest(request, response);
        });
        
        verify(daoCliente, never()).salvar(any(Cliente.class));
    }

    @Test
    void testCadastroClienteInputNull() throws Exception {
        // Arrange
        when(request.getInputStream()).thenReturn(null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            servlet.processRequest(request, response);
        });
        
        verify(daoCliente, never()).salvar(any(Cliente.class));
    }

    @Test
    void testCadastroClienteComCaracteresEspeciais() throws Exception {
        // Arrange
        String jsonInput = "{\"usuario\":{\"nome\":\"João-Pedro\",\"sobrenome\":\"Silva & Santos\",\"telefone\":\"21 98765-4321\",\"usuario\":\"joao.pedro\",\"senha\":\"senha@123\"},"
                + "\"endereco\":{\"rua\":\"Rua das Palmeiras, 100\",\"bairro\":\"Jardim das Flores\",\"numero\":50,\"complemento\":\"Casa 2 - Fundos\",\"cidade\":\"São Gonçalo\",\"estado\":\"RJ\"}}";
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoCliente).salvar(any(Cliente.class));
        writer.flush();
        assertTrue(stringWriter.toString().contains("Usuário Cadastrado!"));
    }

    @Test
    void testCadastroClienteComNumerosGrandes() throws Exception {
        // Arrange
        String jsonInput = "{\"usuario\":{\"nome\":\"Teste\",\"sobrenome\":\"Teste\",\"telefone\":\"21999999999\",\"usuario\":\"teste123\",\"senha\":\"123456\"},"
                + "\"endereco\":{\"rua\":\"Rua Teste\",\"bairro\":\"Bairro Teste\",\"numero\":99999,\"complemento\":\"Apto 999\",\"cidade\":\"Cidade Teste\",\"estado\":\"TS\"}}";
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput));

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoCliente).salvar(any(Cliente.class));
        writer.flush();
        assertTrue(stringWriter.toString().contains("Usuário Cadastrado!"));
    }


}