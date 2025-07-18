package unit;

import Helpers.ValidadorCookie;
import Model.RelatorioGastos;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import Controllers.getRelatorioGastos;
import dao.DaoRelatorio;

@Tag("unit")
public class getRelatorioGastosTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ValidadorCookie validador;
    private DaoRelatorio dao;
    private getRelatorioGastos servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        validador = mock(ValidadorCookie.class);
        dao = mock(DaoRelatorio.class);
        servlet = new getRelatorioGastos(validador, dao);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComTokenValido() throws Exception {
        Cookie[] cookies = { new Cookie("token", "abc123") };
        when(request.getCookies()).thenReturn(cookies);
        when(validador.validar(cookies)).thenReturn(true);

        List<RelatorioGastos> gastos = Arrays.asList(new RelatorioGastos(), new RelatorioGastos());
        when(dao.listarRelGastos()).thenReturn(gastos);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.processRequest(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        String respostaJson = new Gson().toJson(gastos);
        pw.flush();
        assertTrue(sw.toString().contains(respostaJson));
    }

    @Test
    public void testComTokenInvalido() throws Exception {
        Cookie[] cookies = { new Cookie("token", "invalido") };
        when(request.getCookies()).thenReturn(cookies);
        when(validador.validar(cookies)).thenReturn(false);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.processRequest(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        pw.flush();
        assertTrue(sw.toString().contains("Token inválido"));
    }

    @Test
    public void testListaVazia() throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        when(request.getCookies()).thenReturn(new Cookie[] { new Cookie("token", "abc123") });
        when(validador.validar(any())).thenReturn(true);

        when(dao.listarRelGastos()).thenReturn(Arrays.asList());

        servlet.processRequest(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        pw.flush();
        String body = sw.toString();
        assertTrue(body.trim().equals("[]"),
                "Esperava lista vazia '[]', mas recebeu: " + body);
    }

    @Test
    public void testErroInterno() throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        when(request.getCookies())
                .thenReturn(new Cookie[] { new Cookie("token", "abc123") });
        when(validador.validar(any())).thenReturn(true);

        when(dao.listarRelGastos())
                .thenThrow(new RuntimeException("Erro simulado"));

        servlet.processRequest(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        pw.flush();
        String body = sw.toString();
        System.out.println("Resposta: " + body);
        assertTrue(
                body.contains("Falha no servidor"),
                "Esperava 'Falha no servidor' no corpo, mas recebeu: " + body);
    }
        @Test
    public void testDoGet() throws Exception {

        Cookie[] cookies = { new Cookie("token", "get_test_token") };
        when(request.getCookies()).thenReturn(cookies);
        when(validador.validar(cookies)).thenReturn(true);
        when(dao.listarRelGastos()).thenReturn(Arrays.asList(new RelatorioGastos()));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(dao).listarRelGastos();
        writer.flush();
        assertTrue(stringWriter.toString().contains(new Gson().toJson(Arrays.asList(new RelatorioGastos()))),
                   "Resposta inesperada para doGet().");
    }


    @Test
    public void testDoPost() throws Exception {

        Cookie[] cookies = { new Cookie("token", "post_test_token") };
        when(request.getCookies()).thenReturn(cookies);
        when(validador.validar(cookies)).thenReturn(true);
        when(dao.listarRelGastos()).thenReturn(Arrays.asList(new RelatorioGastos()));

        servlet.doPost(request, response); 

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(dao).listarRelGastos();
        writer.flush();
        assertTrue(stringWriter.toString().contains(new Gson().toJson(Arrays.asList(new RelatorioGastos()))),
                   "Resposta inesperada para doPost().");
    }


    @Test
    public void testGetServletInfo() {
        String servletInfo = servlet.getServletInfo(); 
        assertEquals("Short description", servletInfo, 
                     "getServletInfo() não retornou a descrição esperada.");
    }

}
