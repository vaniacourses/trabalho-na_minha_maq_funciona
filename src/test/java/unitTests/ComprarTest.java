package unitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import Model.Lanche;
import Model.Bebida;
import Model.Pedido;
import Model.Cliente;
import Controllers.comprar;
import dao.DaoBebida;
import dao.DaoCliente;
import dao.DaoLanche;
import dao.DaoPedido;
import Helpers.ValidadorCookie;
import jakarta.servlet.http.Cookie;
import org.json.JSONArray;
import org.json.JSONObject;

@Tag("unit")
public class ComprarTest extends BaseServletTest {

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

    @BeforeEach
    void setUp() throws Exception {
        setUpBase();
        servlet = new comprar(daoCliente, daoLanche, daoBebida, daoPedido, validadorCookie);
    }

    @Test
    void testProcessarPedidoComLanche() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        Lanche lanche = new Lanche();
        lanche.setNome("X-Burger");
        lanche.setValor_venda(15.00);
        when(daoLanche.pesquisaPorNome("X-Burger")).thenReturn(lanche);

        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido).vincularLanche(any(Pedido.class), any(Lanche.class));
        assertSuccessResponse();
    }

    @Test
    void testProcessarPedidoComBebida() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        Bebida bebida = new Bebida();
        bebida.setNome("Coca-Cola");
        bebida.setValor_venda(5.00);
        when(daoBebida.pesquisaPorNome("Coca-Cola")).thenReturn(bebida);

        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertSuccessResponse();
    }

    @Test
    void testProcessarPedidoComLancheEBebida() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(2));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        Lanche lanche = new Lanche();
        lanche.setNome("X-Burger");
        lanche.setValor_venda(15.00);
        when(daoLanche.pesquisaPorNome("X-Burger")).thenReturn(lanche);

        Bebida bebida = new Bebida();
        bebida.setNome("Coca-Cola");
        bebida.setValor_venda(5.00);
        when(daoBebida.pesquisaPorNome("Coca-Cola")).thenReturn(bebida);

        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido).vincularLanche(any(Pedido.class), any(Lanche.class));
        verify(daoPedido).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertSuccessResponse();
    }

    @Test
    void testTokenInvalido() throws Exception {
        // Arrange
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));
        when(request.getCookies()).thenReturn(createTestCookies("token", "invalid_token"));
        when(validadorCookie.validar(any())).thenReturn(false);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertErrorResponse();
    }

    @Test
    void testClienteNaoEncontrado() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 999);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(1));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);
        when(daoCliente.pesquisaPorID("999")).thenReturn(null);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertErrorResponse();
    }

    @Test
    void testProdutoNaoEncontrado() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("ProdutoInexistente", new JSONArray().put("15.00").put("lanche").put(1));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);
        when(daoLanche.pesquisaPorNome("ProdutoInexistente")).thenReturn(null);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertErrorResponse();
    }

    @Test
    void testSemCookies() throws Exception {
        // Arrange
        when(request.getInputStream()).thenReturn(new MockServletInputStream("{}"));
        when(request.getCookies()).thenReturn(null);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertErrorResponse();
    }

    @Test
    void testJSONInvalido() throws Exception {
        // Arrange
        when(request.getInputStream()).thenReturn(new MockServletInputStream("json inválido"));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            servlet.processRequest(request, response);
        });
        verify(daoPedido, never()).salvar(any(Pedido.class));
    }

    @Test
    void testPedidoSemItens() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            servlet.processRequest(request, response);
        });
    }

    @Test
    void testPedidoComMultiplosItens() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(2));
        jsonInput.put("Coca-Cola", new JSONArray().put("5.00").put("bebida").put(3));
        jsonInput.put("Batata Frita", new JSONArray().put("8.00").put("lanche").put(1));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        Lanche lanche1 = new Lanche();
        lanche1.setNome("X-Burger");
        lanche1.setValor_venda(15.00);
        when(daoLanche.pesquisaPorNome("X-Burger")).thenReturn(lanche1);

        Bebida bebida = new Bebida();
        bebida.setNome("Coca-Cola");
        bebida.setValor_venda(5.00);
        when(daoBebida.pesquisaPorNome("Coca-Cola")).thenReturn(bebida);

        Lanche lanche2 = new Lanche();
        lanche2.setNome("Batata Frita");
        lanche2.setValor_venda(8.00);
        when(daoLanche.pesquisaPorNome("Batata Frita")).thenReturn(lanche2);

        Pedido pedido = new Pedido();
        pedido.setId_pedido(1);
        when(daoPedido.pesquisaPorData(any(Pedido.class))).thenReturn(pedido);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido).salvar(any(Pedido.class));
        verify(daoPedido, times(2)).vincularLanche(any(Pedido.class), any(Lanche.class));
        verify(daoPedido).vincularBebida(any(Pedido.class), any(Bebida.class));
        assertSuccessResponse();
    }

    @Test
    void testPedidoComQuantidadeZero() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("15.00").put("lanche").put(0));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertErrorResponse();
    }

    @Test
    void testPedidoComPrecoNegativo() throws Exception {
        // Arrange
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("id", 1);
        jsonInput.put("X-Burger", new JSONArray().put("-15.00").put("lanche").put(1));

        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonInput.toString()));
        when(request.getCookies()).thenReturn(createValidTokenCookies());
        when(validadorCookie.validar(any())).thenReturn(true);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(1);
        when(daoCliente.pesquisaPorID("1")).thenReturn(cliente);

        // Act
        servlet.processRequest(request, response);

        // Assert
        verify(daoPedido, never()).salvar(any(Pedido.class));
        assertErrorResponse();
    }
}