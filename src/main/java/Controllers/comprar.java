/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Helpers.ValidadorCookie;
import Model.Bebida;
import Model.Cliente;
import Model.Lanche;
import Model.Pedido;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import dao.DaoBebida;
import dao.DaoCliente;
import dao.DaoLanche;
import dao.DaoPedido;

/**
 *
 * @author kener_000
 */
public class comprar extends HttpServlet {

    private final DaoCliente daoCliente;
    private final DaoLanche daoLanche;
    private final DaoBebida daoBebida;
    private final DaoPedido daoPedido;
    private final ValidadorCookie validadorCookie;

    public comprar() {
        this.daoCliente = new DaoCliente();
        this.daoLanche = new DaoLanche();
        this.daoBebida = new DaoBebida();
        this.daoPedido = new DaoPedido();
        this.validadorCookie = new ValidadorCookie();
    }

    // Construtor para testes
    public comprar(DaoCliente daoCliente, DaoLanche daoLanche, DaoBebida daoBebida, 
                  DaoPedido daoPedido, ValidadorCookie validadorCookie) {
        this.daoCliente = daoCliente;
        this.daoLanche = daoLanche;
        this.daoBebida = daoBebida;
        this.daoPedido = daoPedido;
        this.validadorCookie = validadorCookie;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        
        ////////Validar Cookie
        boolean resultado = false;
        
        try {
            Cookie[] cookies = request.getCookies();
            resultado = validadorCookie.validar(cookies);
        } catch(java.lang.NullPointerException e) {}
        //////////////
        
        if ((br != null) && resultado) {
            json = br.readLine();
            byte[] bytes = json.getBytes(ISO_8859_1); 
            String jsonStr = new String(bytes, UTF_8);            
            JSONObject dados = new JSONObject(jsonStr);
            
            Cliente cliente = daoCliente.pesquisaPorID(String.valueOf(dados.getInt("id")));
            
            if (cliente == null) {
                try (PrintWriter out = response.getWriter()) {
                    out.println("erro");
                }
                return;
            }
            
            Iterator<String> keys = dados.keys();
            
            Double valor_total = 0.00;
            
            List<Lanche> lanches = new ArrayList<Lanche>();
            List<Bebida> bebidas = new ArrayList<Bebida>();
            
            while(keys.hasNext()) {
                String nome = keys.next();
                if(!nome.equals("id")){
                    if(dados.getJSONArray(nome).get(1).equals("lanche")){
                        Lanche lanche = daoLanche.pesquisaPorNome(nome);
                        if (lanche == null) {
                            try (PrintWriter out = response.getWriter()) {
                                out.println("erro");
                            }
                            return;
                        }
                        int quantidade = dados.getJSONArray(nome).getInt(2);
                        lanche.setQuantidade(quantidade);
                        valor_total += lanche.getValor_venda();
                        lanches.add(lanche);
                    }
                    if(dados.getJSONArray(nome).get(1).equals("bebida")){
                        Bebida bebida = daoBebida.pesquisaPorNome(nome);
                        if (bebida == null) {
                            try (PrintWriter out = response.getWriter()) {
                                out.println("erro");
                            }
                            return;
                        }
                        int quantidade = dados.getJSONArray(nome).getInt(2);
                        bebida.setQuantidade(quantidade);
                        valor_total += bebida.getValor_venda();
                        bebidas.add(bebida);
                    }
                }
            }
            
            Pedido pedido = new Pedido();
            pedido.setData_pedido(Instant.now().toString());
            pedido.setCliente(cliente);
            pedido.setValor_total(valor_total);
            daoPedido.salvar(pedido);
            pedido = daoPedido.pesquisaPorData(pedido);
            pedido.setCliente(cliente);
            
            for(int i = 0; i<lanches.size(); i++){
                daoPedido.vincularLanche(pedido, lanches.get(i));
            }
            for(int i = 0; i<bebidas.size(); i++){
                daoPedido.vincularBebida(pedido, bebidas.get(i));
            }
  
            try (PrintWriter out = response.getWriter()) {
                out.println("Pedido Salvo com Sucesso!");
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("erro");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
