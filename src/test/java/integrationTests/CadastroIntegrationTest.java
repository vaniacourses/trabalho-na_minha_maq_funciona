
package integrationTests;

import Controllers.cadastro;
import DAO.DaoCliente;
import DAO.DaoEndereco;
import DAO.DaoUtil;
import Model.Cliente;
import Model.Endereco;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Tag;

@Tag("integration")
public class CadastroIntegrationTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private cadastro servlet;
    private DaoCliente daoCliente;
    private DaoEndereco daoEndereco;
    private StringWriter stringWriter;
    private PrintWriter writer;
    private Connection adminConnection; 
    private Connection testDbConnection;


    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        String defaultJdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "postgres";
        String dbName = "lanchonete";
        String bancoSqlPath = "banco.sql";

        try {

            adminConnection = DriverManager.getConnection(defaultJdbcUrl, dbUser, dbPassword);
            adminConnection.setAutoCommit(true);
            try (Statement stmt = adminConnection.createStatement()) {
                System.out.println("Dropando a base " + dbName );
                stmt.execute("DROP DATABASE IF EXISTS " + dbName + " WITH (FORCE);");
                System.out.println("Apaguei " + dbName );
            }
            try (Statement stmt = adminConnection.createStatement()) {
                System.out.println("Criando " + dbName);
                stmt.execute("CREATE DATABASE " + dbName + ";");
                System.out.println("Criei " + dbName );
            }
            adminConnection.close();
            System.out.println("Conectando ao banco de dados para inicializar o esquema");
            testDbConnection = new DaoUtil().conecta(); 
            testDbConnection.setAutoCommit(false);

            System.out.println("Executando banco.sql...");
            String sqlScript = Files.readString(Paths.get(bancoSqlPath)); 
            try (Statement stmt = testDbConnection.createStatement()) {
                Stream.of(sqlScript.split(";"))
                      .filter(s -> !s.trim().isEmpty())
                      .forEach(s -> {
                          try {
                              stmt.execute(s + ";"); 
                          } catch (SQLException e) {
                              throw new RuntimeException("Erro ao executar parte do script SQL: " + s + "\nErro: " + e.getMessage(), e);
                          }
                      });
            }
            testDbConnection.commit(); 
            System.out.println("banco.sql executado com sucesso e banco pronto para o teste!");
            
        } catch (Exception e) { 
            System.err.println("Falhou miseravelmente: " + e.getMessage());
            throw new RuntimeException("Não funcionou.", e);
        } finally {

            if (adminConnection != null && !adminConnection.isClosed()) {
                try { adminConnection.close(); } catch (SQLException e) {}
            }
            if (testDbConnection != null && !testDbConnection.isClosed()) {
                try { testDbConnection.close(); } catch (SQLException e) {}
            }
        }
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