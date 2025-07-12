
package integrationTests;

import Controllers.cadastro;
import dao.DaoCliente;
import dao.DaoEndereco;
import Model.Cliente;
import Model.Endereco;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("integration")
public class CadastroIntegrationTest extends BaseIntegrationTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private cadastro servlet;
    private DaoCliente daoCliente;
    private DaoEndereco daoEndereco;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        // Chama o setup da classe pai
        super.setUpIntegration();
        
        // Setup dos mocks
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Inicializa os DAOs usando a conexão da classe pai
        daoCliente = new DaoCliente();
        daoEndereco = new DaoEndereco();
        servlet = new cadastro(daoCliente);
    }

    @Test
    void testCadastroClienteComEnderecoSucesso() throws Exception {
        JSONObject enderecoJson = new JSONObject();
        enderecoJson.put("bairro", "Bairro Teste");
        enderecoJson.put("cidade", "Cidade Teste");
        enderecoJson.put("estado", "RJ");
        enderecoJson.put("complemento", "Apto 101");
        enderecoJson.put("rua", "Rua Teste");
        enderecoJson.put("numero", 123);

        JSONObject usuarioJson = new JSONObject();
        usuarioJson.put("nome", "Teste");
        usuarioJson.put("sobrenome", "Sobrenome");
        usuarioJson.put("telefone", "21997654321"); 
        usuarioJson.put("usuario", "testeuser");
        usuarioJson.put("senha", "testesenha");

        JSONObject fullJson = new JSONObject();
        fullJson.put("endereco", enderecoJson);
        fullJson.put("usuario", usuarioJson);
        
        when(request.getInputStream()).thenReturn(new MockServletInputStream(fullJson.toString()));
        servlet.doPost(request, response);
        assertTrue(stringWriter.toString().contains("Usuário Cadastrado!"), "Não teve mensagem de sucesso.");
        
        Cliente buscaCliente = new Cliente();
        buscaCliente.setUsuario("testeuser");
        Cliente clienteRetorno = daoCliente.pesquisaPorUsuario(buscaCliente);

        assertNotNull(clienteRetorno, "Cliente está na base");
        assertTrue(clienteRetorno.getNome().equals("Teste"), "Nome do cliente diferente");
        assertTrue(clienteRetorno.getSobrenome().equals("Sobrenome"), "Sobrenome do cliente diferente");
        assertTrue(clienteRetorno.getTelefone().equals("21997654321"), "Telefone do cliente diferente");
        assertTrue(clienteRetorno.getUsuario().equals("testeuser"), "Usuário do cliente diferente");

        Endereco persistedAddress = daoEndereco.pesquisarEnderecoPorID(clienteRetorno.getId_cliente());
        
        assertNotNull(persistedAddress, "Endereço não está na base");
        assertTrue(persistedAddress.getRua().equals("Rua Teste"), "Rua do endereço diferente");
        assertTrue(persistedAddress.getNumero() == 123, "Número do endereço diferente");
        assertTrue(persistedAddress.getBairro().equals("Bairro Teste"), "Bairro do endereço diferente");
        assertTrue(persistedAddress.getCidade().equals("Cidade Teste"), "Cidade do endereço diferente");
        assertTrue(persistedAddress.getEstado().equals("RJ"), "Estado do endereço diferente");
        assertTrue(persistedAddress.getComplemento().equals("Apto 101"), "Complemento do endereço diferente");
    }

    private static class MockServletInputStream extends jakarta.servlet.ServletInputStream {
        private final StringReader reader;

        public MockServletInputStream(String content) {
            this.reader = new StringReader(content);
        }

        @Override
        public int read() throws java.io.IOException {
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
        }
    }
}