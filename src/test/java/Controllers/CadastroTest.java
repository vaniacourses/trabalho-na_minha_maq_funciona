package Controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import DAO.DaoCliente;
import Model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

public class CadastroTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private DaoCliente daoCliente;

    private cadastro servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet = new cadastro(daoCliente); // injeção do mock
    }

    @Test
    void testCadastroClienteSucesso() throws Exception {
        String jsonInput =
            "{\"usuario\":{\"nome\":\"Veron\",\"sobrenome\":\"Lessa\",\"telefone\":\"21 99999-9999\",\"usuario\":\"Velessa\",\"senha\":\"123456\"},"
          + "\"endereco\":{\"rua\":\"Rua dois\",\"bairro\":\"Gávea\",\"numero\":10,\"complemento\":\"Cobertura\",\"cidade\":\"Rio de Janeiro\",\"estado\":\"Rio de Janeiro\"}}";
        when(request.getInputStream()).thenReturn(new ServletInputStreamMock(jsonInput));

        servlet.processRequest(request, response);


        verify(daoCliente).salvar(any(Cliente.class));
        assertTrue(stringWriter.toString().contains("Usuário Cadastrado!"));
    }
}


class ServletInputStreamMock extends jakarta.servlet.ServletInputStream {
    private final ByteArrayInputStream bais;

    public ServletInputStreamMock(String str) {
        this.bais = new ByteArrayInputStream(str.getBytes());
    }

    @Override
    public int read() throws IOException {
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