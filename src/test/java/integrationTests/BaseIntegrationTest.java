package integrationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe base para testes de integração
 * Configura conexão com banco de dados de teste
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {
    
    protected Connection connection;
    protected static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/lanchonete";
    protected static final String TEST_DB_USER = "postgres";
    protected static final String TEST_DB_PASSWORD = "postgres";
    
    @BeforeEach
    void setUpIntegration() throws SQLException {
        connection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
        connection.setAutoCommit(true);
        limparDadosTeste(); // Reativado para garantir isolamento dos testes
    }
    
    @AfterEach
    void tearDownIntegration() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // connection.rollback(); // Comentado porque autoCommit está true
            connection.close();
        }
    }
    
    /**
     * Limpa dados de teste antes de cada teste
     */
    protected void limparDadosTeste() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Limpar tabelas na ordem correta (respeitando foreign keys)
            stmt.execute("DELETE FROM tb_bebidas_pedido");
            stmt.execute("DELETE FROM tb_lanches_pedido");
            stmt.execute("DELETE FROM tb_pedidos");
            stmt.execute("DELETE FROM tb_ingredientes_lanche");
            stmt.execute("DELETE FROM tb_lanches");
            stmt.execute("DELETE FROM tb_bebidas");
            stmt.execute("DELETE FROM tb_ingredientes");
            stmt.execute("DELETE FROM tb_clientes");
            stmt.execute("DELETE FROM tb_funcionarios");
            stmt.execute("DELETE FROM tb_enderecos");
            stmt.execute("DELETE FROM tb_tokens");
            // Resetar sequências de IDs
            stmt.execute("ALTER SEQUENCE tb_clientes_id_cliente_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_enderecos_id_endereco_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_funcionarios_id_funcionario_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_ingredientes_id_ingrediente_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_lanches_id_lanche_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_pedidos_id_pedido_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_bebidas_id_bebida_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE tb_tokens_id_token_seq RESTART WITH 1");
        }
    }
    
    /**
     * Insere dados de teste básicos
     */
    protected void inserirDadosTeste() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Inserir endereço de teste
            stmt.execute("INSERT INTO tb_enderecos (rua, bairro, numero, complemento, cidade, estado) " +
                        "VALUES ('Rua Teste', 'Bairro Teste', 123, 'Apto 1', 'Cidade Teste', 'TS')");
            
            // Inserir cliente de teste usando o ID do endereço que acabamos de inserir
            stmt.execute("INSERT INTO tb_clientes (nome, sobrenome, telefone, usuario, senha, fg_ativo, id_endereco) " +
                        "SELECT 'Cliente', 'Teste', '11999999999', 'cliente_teste', MD5('senha123'), 1, id_endereco " +
                        "FROM tb_enderecos WHERE rua = 'Rua Teste' AND bairro = 'Bairro Teste'");
            
            // Inserir funcionário de teste
            stmt.execute("INSERT INTO tb_funcionarios (nome, sobrenome, cargo, salario, usuario, senha, fg_ativo) " +
                        "VALUES ('Funcionario', 'Teste', 'Gerente', 3000.00, 'func_teste', MD5('senha123'), 1)");
            
            // Inserir ingredientes de teste
            stmt.execute("INSERT INTO tb_ingredientes (nm_ingrediente, descricao, valor_compra, valor_venda, quantidade, tipo, fg_ativo) " +
                        "VALUES ('Pão', 'Pão de hambúrguer', 1.00, 2.00, 100, 'Pão', 1)");
            stmt.execute("INSERT INTO tb_ingredientes (nm_ingrediente, descricao, valor_compra, valor_venda, quantidade, tipo, fg_ativo) " +
                        "VALUES ('Hambúrguer', 'Carne de hambúrguer', 3.00, 6.00, 50, 'Carne', 1)");
            
            // Inserir bebida de teste
            stmt.execute("INSERT INTO tb_bebidas (nm_bebida, descricao, valor_compra, valor_venda, quantidade, tipo, fg_ativo) " +
                        "VALUES ('Coca-Cola', 'Refrigerante Coca-Cola 350ml', 2.00, 5.00, 100, 'Refrigerante', 1)");
        }
    }
} 