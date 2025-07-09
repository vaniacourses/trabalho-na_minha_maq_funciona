package Controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import DAO.DaoCliente;
import DAO.DaoEndereco;
import Model.Cliente;
import Model.Endereco;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Testes de integração para cadastro de clientes
 * Testa a integração real com o banco de dados
 */
@Tag("integration")
public class CadastroIntegrationTest extends BaseIntegrationTest {
    
    private DaoCliente daoCliente;
    private DaoEndereco daoEndereco;
    
    @BeforeEach
    void setUp() throws Exception {
        setUpIntegration();
        daoCliente = new DaoCliente();
        daoEndereco = new DaoEndereco();
        inserirDadosTeste();
    }
    
    @Test
    void testCadastroClienteComEnderecoNovo() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setSobrenome("Silva");
        cliente.setTelefone("11988887777");
        cliente.setUsuario("joao.silva");
        cliente.setSenha("senha123");
        cliente.setFg_ativo(1);
        
        Endereco endereco = new Endereco();
        endereco.setRua("Rua das Flores");
        endereco.setBairro("Centro");
        endereco.setNumero(456);
        endereco.setComplemento("Sala 10");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        
        cliente.setEndereco(endereco);
        
        // Act
        daoCliente.salvar(cliente);
        
        // Assert
        List<Cliente> clientes = daoCliente.listarTodos();
        assertTrue(clientes.stream().anyMatch(c -> c.getUsuario().equals("joao.silva")));
        
        // Verificar se o endereço foi salvo
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_enderecos WHERE rua = 'Rua das Flores'");
            rs.next();
            assertTrue(rs.getInt(1) > 0, "Endereço deveria ter sido salvo");
        }
    }
    
    @Test
    void testCadastroClienteComEnderecoExistente() throws Exception {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setNome("Maria");
        cliente1.setSobrenome("Santos");
        cliente1.setTelefone("11977776666");
        cliente1.setUsuario("maria.santos");
        cliente1.setSenha("senha456");
        cliente1.setFg_ativo(1);
        
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Teste");
        endereco.setBairro("Bairro Teste");
        endereco.setNumero(123);
        endereco.setComplemento("Apto 1");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("TS");
        
        cliente1.setEndereco(endereco);
        
        Cliente cliente2 = new Cliente();
        cliente2.setNome("Pedro");
        cliente2.setSobrenome("Oliveira");
        cliente2.setTelefone("11966665555");
        cliente2.setUsuario("pedro.oliveira");
        cliente2.setSenha("senha789");
        cliente2.setFg_ativo(1);
        cliente2.setEndereco(endereco); // Mesmo endereço
        
        // Act
        daoCliente.salvar(cliente1);
        daoCliente.salvar(cliente2);
        
        // Assert
        List<Cliente> clientes = daoCliente.listarTodos();
        assertTrue(clientes.stream().anyMatch(c -> c.getUsuario().equals("maria.santos")));
        assertTrue(clientes.stream().anyMatch(c -> c.getUsuario().equals("pedro.oliveira")));
        
        // Verificar se apenas um endereço foi criado
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tb_enderecos WHERE rua = 'Rua Teste'");
            rs.next();
            assertEquals(1, rs.getInt(1), "Deveria existir apenas um endereço");
        }
    }
    
    @Test
    void testLoginClienteValido() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setUsuario("cliente_teste");
        cliente.setSenha("senha123");
        
        // Act
        boolean loginSucesso = daoCliente.login(cliente);
        
        // Assert
        assertTrue(loginSucesso, "Login deveria ser bem-sucedido");
    }
    
    @Test
    void testLoginClienteInvalido() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setUsuario("usuario_inexistente");
        cliente.setSenha("senha123");
        
        // Act
        boolean loginSucesso = daoCliente.login(cliente);
        
        // Assert
        assertFalse(loginSucesso, "Login deveria falhar");
    }
    
    @Test
    void testLoginClienteSenhaIncorreta() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setUsuario("cliente_teste");
        cliente.setSenha("senha_incorreta");
        
        // Act
        boolean loginSucesso = daoCliente.login(cliente);
        
        // Assert
        assertFalse(loginSucesso, "Login deveria falhar com senha incorreta");
    }
    
    @Test
    void testPesquisaClientePorID() throws Exception {
        // Act
        Cliente cliente = daoCliente.pesquisaPorID("1");
        
        // Assert
        assertNotNull(cliente, "Cliente deveria ser encontrado");
        assertEquals("Cliente", cliente.getNome());
        assertEquals("Teste", cliente.getSobrenome());
    }
    
    @Test
    void testPesquisaClientePorUsuario() throws Exception {
        // Arrange
        Cliente clienteBusca = new Cliente();
        clienteBusca.setUsuario("cliente_teste");
        
        // Act
        Cliente cliente = daoCliente.pesquisaPorUsuario(clienteBusca);
        
        // Assert
        assertNotNull(cliente, "Cliente deveria ser encontrado");
        assertEquals("cliente_teste", cliente.getUsuario());
    }
    
    @Test
    void testListarTodosClientes() throws Exception {
        // Act
        List<Cliente> clientes = daoCliente.listarTodos();
        
        // Assert
        assertNotNull(clientes, "Lista não deveria ser nula");
        assertTrue(clientes.size() > 0, "Deveria haver pelo menos um cliente");
        
        // Verificar se todos os clientes estão ativos
        clientes.forEach(cliente -> 
            assertEquals(1, cliente.getFg_ativo(), "Todos os clientes deveriam estar ativos")
        );
    }
} 