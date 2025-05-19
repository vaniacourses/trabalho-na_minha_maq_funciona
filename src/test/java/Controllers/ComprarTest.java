package Controllers;

import DAO.DaoBebida;
import DAO.DaoCliente;
import DAO.DaoLanche;
import DAO.DaoPedido;
import Helpers.ValidadorCookie;
import Model.Bebida;
import Model.Cliente;
import Model.Lanche;
import Model.Pedido;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ComprarTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private DaoCliente daoCliente;
    
    @Mock
    private DaoLanche daoLanche;
    
    @Mock
    private DaoBebida daoBebida;
    
    @Mock
    private DaoPedido daoPedido;
    
    @Mock
    private ValidadorCookie validadorCookie;
    
    private comprar servlet;
    
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet = new comprar(daoCliente, daoLanche, daoBebida, daoPedido, validadorCookie);
    }

    @Test
    void testProcessarPedidoComLanche() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Mock do lanche
        Lanche lanche = new Lanche();
        lanche.setNome("X-Burger");
        lanche.setValor_venda(15.00);
        when(daoLanche.pesquisaPorNome("X-Burger")).thenReturn(lanche);

        // Mock do pedido salvo
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido).vincularLanche(any(Pedido.class), any(Lanche.class));
        assertTrue(stringWriter.toString().contains("Pedido Salvo com Sucesso!"));
    }

    @Test
    void testProcessarPedidoComBebida() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Mock da bebida
        Bebida bebida = new Bebida();
        bebida.setNome("Coca-Cola");
        bebida.setValor_venda(5.00);
        when(daoBebida.pesquisaPorNome("Coca-Cola")).thenReturn(bebida);

        // Mock do pedido salvo
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertTrue(stringWriter.toString().contains("Pedido Salvo com Sucesso!"));
    }

    @Test
    void testProcessarPedidoComLancheEBebida() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Mock do lanche
        Lanche lanche = new Lanche();
        lanche.setNome("X-Burger");
        lanche.setValor_venda(15.00);
        when(daoLanche.pesquisaPorNome("X-Burger")).thenReturn(lanche);

        // Mock da bebida
        Bebida bebida = new Bebida();
        bebida.setNome("Coca-Cola");
        bebida.setValor_venda(5.00);
        when(daoBebida.pesquisaPorNome("Coca-Cola")).thenReturn(bebida);

        // Mock do pedido salvo
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido).vincularLanche(any(Pedido.class), any(Lanche.class));
        verify(daoPedido).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertTrue(stringWriter.toString().contains("Pedido Salvo com Sucesso!"));
    }

    @Test
    void testTokenInvalido() throws Exception {
        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));

        // Mock dos cookies inválidos
        Cookie[] cookies = {new Cookie("token", "invalid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(false);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }

    @Test
    void testClienteNaoEncontrado() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 999); // ID de cliente inexistente
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente não encontrado
        when(daoCliente.pesquisaPorID("999")).thenReturn(null);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }

    @Test
    void testProdutoNaoEncontrado() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("ProdutoInexistente", new JSONArray().put("15.00").put("lanche").put(1));

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Mock do produto não encontrado
        when(daoLanche.pesquisaPorNome("ProdutoInexistente")).thenReturn(null);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }

    @Test
    void testSemCookies() throws Exception {
        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));

        // Mock sem cookies
        when(request.getCookies()).thenReturn(null);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }

    @Test
    void testJSONInvalido() throws Exception {
        // Mock do BufferedReader com JSON inválido (formato de item inválido)
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{\"id\": 1, \"X-Burger\": [\"15.00\", \"lanche\"]}"));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertTrue(stringWriter.toString().contains("erro"));
    }

    @Test
    void testPedidoSemItens() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Mock do pedido salvo
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido, never()).vincularLanche(any(Pedido.class), any(Lanche.class));
        verify(daoPedido, never()).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertTrue(stringWriter.toString().contains("Pedido Salvo com Sucesso!"));
    }

    @Test
    void testPedidoComMultiplosItens() throws Exception {
        // Preparar dados de teste
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(2));
        jsonInput.put("X-Bacon", new JSONArray().put("18.00").put("lanche").put(1));
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));
        jsonInput.put("Suco", new JSONArray().put("6.00").put("bebida").put(1));

        // Mock do BufferedReader
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));

        // Mock dos cookies
        Cookie[] cookies = {new Cookie("token", "valid_token")};
        when(request.getCookies()).thenReturn(cookies);
        when(validadorCookie.validar(cookies)).thenReturn(true);

        // Mock do cliente
        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Mock dos lanches
        Lanche xBurger = new Lanche();
        xBurger.setNome("X-Burger");
        xBurger.setValor_venda(15.00);
        when(daoLanche.pesquisaPorNome("X-Burger")).thenReturn(xBurger);

        Lanche xBacon = new Lanche();
        xBacon.setNome("X-Bacon");
        xBacon.setValor_venda(18.00);
        when(daoLanche.pesquisaPorNome("X-Bacon")).thenReturn(xBacon);

        // Mock das bebidas
        Bebida cocaCola = new Bebida();
        cocaCola.setNome("Coca-Cola");
        cocaCola.setValor_venda(5.00);
        when(daoBebida.pesquisaPorNome("Coca-Cola")).thenReturn(cocaCola);

        Bebida suco = new Bebida();
        suco.setNome("Suco");
        suco.setValor_venda(6.00);
        when(daoBebida.pesquisaPorNome("Suco")).thenReturn(suco);

        // Mock do pedido salvo
        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Executar o teste
        servlet.processRequest(request, response);

        // Verificar resultados
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido, times(2)).vincularLanche(any(Pedido.class), any(Lanche.class));
        verify(daoPedido, times(2)).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertTrue(stringWriter.toString().contains("Pedido Salvo com Sucesso!"));
    }

    class MockServletInputStream extends ServletInputStream {
        private final StringReader reader;

        public MockServletInputStream(String content) {
            this.reader = new StringReader(content);
        }

        @Override
        public int read() throws IOException {
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
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}