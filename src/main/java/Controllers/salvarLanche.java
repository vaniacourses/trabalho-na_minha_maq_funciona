/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Helpers.ValidadorCookie;
import Model.Ingrediente;
import Model.Lanche;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Iterator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import dao.DaoIngrediente;
import dao.DaoLanche;

/**
 *
 * @author kener_000
 */
public class salvarLanche extends HttpServlet {
    private DaoLanche lancheDao;
    private DaoIngrediente ingredienteDao;
    private ValidadorCookie validadorCookie;

    public salvarLanche() {
        this.lancheDao = new DaoLanche();
        this.ingredienteDao = new DaoIngrediente();
        this.validadorCookie = new ValidadorCookie();
    }

    public salvarLanche(DaoLanche lancheDao, DaoIngrediente ingredienteDao, ValidadorCookie validadorCookie) {
        this.lancheDao = lancheDao;
        this.ingredienteDao = ingredienteDao;
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";

        boolean resultado = false;
        try {
            Cookie[] cookies = request.getCookies();
            resultado = validadorCookie.validarFuncionario(cookies);
        } catch (java.lang.NullPointerException e) {}

        if ((br != null) && resultado) {
            json = br.readLine();
            byte[] bytes = json.getBytes(ISO_8859_1);
            String jsonStr = new String(bytes, UTF_8);
            JSONObject dados = new JSONObject(jsonStr);
            JSONObject ingredientes = dados.getJSONObject("ingredientes");

            Lanche lanche = new Lanche();
            lanche.setNome(dados.getString("nome"));
            lanche.setDescricao(dados.getString("descricao"));
            lanche.setValor_venda(dados.getDouble("ValorVenda"));

            lancheDao.salvar(lanche);
            Lanche lancheComID = lancheDao.pesquisaPorNome(lanche);

            Iterator<String> keys = ingredientes.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Ingrediente ingredienteLanche = new Ingrediente();
                ingredienteLanche.setNome(key);
                Ingrediente ingredienteComID = ingredienteDao.pesquisaPorNome(ingredienteLanche);
                ingredienteComID.setQuantidade(ingredientes.getInt(key));
                lancheDao.vincularIngrediente(lancheComID, ingredienteComID);
            }

            try (PrintWriter out = response.getWriter()) {
                out.println("Lanche Salvo com Sucesso!");
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
