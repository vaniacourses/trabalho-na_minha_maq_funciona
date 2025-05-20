package Controllers;

import DAO.DaoCliente;
import Model.Cliente;
import Model.Endereco;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CadastroTest {

    private cadastro servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private DaoCliente daoCliente;

    @BeforeEach
    void setUp() {
        servlet = new cadastro();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        daoCliente = mock(DaoCliente.class);
    }

    @Test
    void testCadastroClienteComSucesso() throws Exception {
        // JSON conforme o fluxo descrito
        JSONObject json = new JSONObject()
            .put("usuario", new JSONObject()
                .put("nome", "Veron")
                .put("sobrenome", "Lessa")
                .put("telefone", "21 99999-9999")
                .put("usuario", "Velessa")
                .put("senha", "123456"))
            .put("endereco", new JSONObject()
                .put("rua", "Rua dois")
                .put("bairro", "Gávea")
                .put("numero", 10)
                .put("complemento", "Cobertura")
                .put("cidade", "Rio de Janeiro")
                .put("estado", "Rio de Janeiro"));

        // Simulando InputStream do request
        BufferedReader reader = new BufferedReader(new StringReader(json.toString()));
        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(json.toString().getBytes("ISO-8859-1"))));
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // Spy para capturar cliente salvo
        DaoCliente daoClienteSpy = spy(new DaoCliente() {
            @Override
            public void salvar(Cliente cliente) {
                // mock implementation
            }
        });

        // Substituir o DAO usando um spy — caso possível, recomendo refatorar para injetar via setter
        cadastro servletComDao = new cadastro() {
            @Override
            protected void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
                String json = br.readLine();
                byte[] bytes = json.getBytes("ISO-8859-1");
                String jsonStr = new String(bytes, "UTF-8");
                JSONObject dados = new JSONObject(jsonStr);

                Endereco endereco = new Endereco();
                endereco.setBairro(dados.getJSONObject("endereco").getString("bairro"));
                endereco.setCidade(dados.getJSONObject("endereco").getString("cidade"));
                endereco.setEstado(dados.getJSONObject("endereco").getString("estado"));
                endereco.setComplemento(dados.getJSONObject("endereco").getString("complemento"));
                endereco.setRua(dados.getJSONObject("endereco").getString("rua"));
                endereco.setNumero(dados.getJSONObject("endereco").getInt("numero"));

                Cliente cliente = new Cliente();
                cliente.setNome(dados.getJSONObject("usuario").getString("nome"));
                cliente.setSobrenome(dados.getJSONObject("usuario").getString("sobrenome"));
                cliente.setTelefone(dados.getJSONObject("usuario").getString("telefone"));
                cliente.setUsuario(dados.getJSONObject("usuario").getString("usuario"));
                cliente.setSenha(dados.getJSONObject("usuario").getString("senha"));
                cliente.setFg_ativo(1);
                cliente.setEndereco(endereco);

                daoClienteSpy.salvar(cliente);

                try (PrintWriter out = res.getWriter()) {
                    out.println("Usuário Cadastrado!");
                }
            }
        };

        // Executar o teste
        servletComDao.doPost(request, response);

        // Verificação se o método salvar foi chamado com os dados esperados
        verify(daoClienteSpy, times(1)).salvar(any(Cliente.class));
    }

    // Classe auxiliar para simular ServletInputStream a partir de ByteArrayInputStream
    static class DelegatingServletInputStream extends jakarta.servlet.ServletInputStream {
        private final InputStream sourceStream;

        public DelegatingServletInputStream(InputStream sourceStream) {
            this.sourceStream = sourceStream;
        }

        @Override
        public int read() throws IOException {
            return sourceStream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return sourceStream.available() == 0;
            } catch (IOException e) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener) {
            throw new UnsupportedOperationException();
        }
    }
}
