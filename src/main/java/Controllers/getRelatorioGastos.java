/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DAO.DaoRelatorio;
import Helpers.ValidadorCookie;
import Model.RelatorioGastos;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author gabriel
 */
public class getRelatorioGastos extends HttpServlet {
    private ValidadorCookie validador;
    private DaoRelatorio dao;

    public getRelatorioGastos() {
        this.validador = new ValidadorCookie();
        this.dao = new DaoRelatorio();
    }

    public getRelatorioGastos(ValidadorCookie validador, DaoRelatorio dao) {
        this.validador = validador;
        this.dao = dao;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // pega o writer fora do try-with-resources, sem fechar automático
    PrintWriter out = response.getWriter();
    try {
        Cookie[] cookies = request.getCookies();
        boolean valido = validador.validar(cookies);

        if (!valido) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"erro\":\"Token inválido\"}");
            return;
        }

        List<RelatorioGastos> lista = dao.listarRelGastos();
        String json = new Gson().toJson(lista);
        out.print(json);

    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        // usa o mesmo 'out' sem reabrir nem fechar
        out.write("Falha no servidor");
    }
}


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                    try {
       processRequest(request, response);
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print("Falha no servidor");
    }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
