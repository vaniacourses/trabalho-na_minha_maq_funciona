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
    
    private final ValidadorCookie validador;
    private final DaoRelatorio dao;

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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            Cookie[] cookies = request.getCookies();
            boolean resultado = validador.validar(cookies);
            
            if(resultado) {
                List<RelatorioGastos> relatorio = dao.listarRelGastos();
                Gson gson = new Gson();
                String json = gson.toJson(relatorio);

                try (PrintWriter out = response.getWriter()) {
                    out.print(json);
                    out.flush();
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                try (PrintWriter out = response.getWriter()) {
                    out.print("Token inválido");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.print("Falha no servidor");
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
    public void doGet(HttpServletRequest request, HttpServletResponse response)
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
    public void doPost(HttpServletRequest request, HttpServletResponse response)
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