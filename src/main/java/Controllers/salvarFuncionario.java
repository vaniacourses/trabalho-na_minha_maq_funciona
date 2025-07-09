/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import DAO.DaoFuncionario;
import Helpers.ValidadorCookie;
import Model.Funcionario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author kener_000
 */
public class salvarFuncionario extends HttpServlet {
    private final DaoFuncionario daoFuncionario;
    private final ValidadorCookie validadorCookie;

    public salvarFuncionario(DaoFuncionario daoFuncionario, ValidadorCookie validadorCookie) {
        this.daoFuncionario = daoFuncionario;
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
        String ID = "1";
        boolean resultado = false;
        
        try {
            Cookie[] cookies = request.getCookies();
            ID = validadorCookie.getCookieIdFuncionario(cookies);
            resultado = validadorCookie.validarFuncionario(cookies);
        } catch(java.lang.NullPointerException e) {
            System.out.println(e);
        }
        
        if ((br != null) && resultado) {
            json = br.readLine();
            byte[] bytes = json.getBytes(ISO_8859_1); 
            String jsonStr = new String(bytes, UTF_8);            
            JSONObject dados = new JSONObject(jsonStr);
            
            // Validações dos dados
            String nome = dados.getString("nome");
            double salario = dados.getDouble("salario");
            String usuario = dados.getString("usuarioFuncionario");
            String senha = dados.getString("senhaFuncionario");
            
            if (nome.isEmpty() || salario <= 0 || usuario.isEmpty() || senha.isEmpty()) {
                try (PrintWriter out = response.getWriter()) {
                    out.println("erro");
                }
                return;
            }
            
            Funcionario funcionario = new Funcionario();
            funcionario.setCad_por(Integer.valueOf(ID));
            funcionario.setNome(nome);
            funcionario.setCargo("cargo");
            funcionario.setSalario(salario);
            funcionario.setUsuario(usuario);
            funcionario.setSenha(senha);
            funcionario.setSobrenome("sobrenome");
            funcionario.setFg_ativo(1);
            
            daoFuncionario.salvar(funcionario);
            
            try (PrintWriter out = response.getWriter()) {
                out.println("Funcionario Cadastrado!");
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
