/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Cliente;
import Model.Endereco;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import dao.DaoCliente;

/**
 *
 * @author kener_000
 */
@WebServlet(name = "cadastro", urlPatterns = {"/cadastro"})
public class cadastro extends HttpServlet {

    private final DaoCliente daoCliente;

    public cadastro () {
        this.daoCliente = new DaoCliente();
    }

    public cadastro (DaoCliente daoCliente) {
        this.daoCliente = daoCliente;
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
        
        //Seta o tipo de Conteudo que será recebido, nesse caso, um JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        //Pra receber JSONs, é necessario utilizar esse Buffer pra receber os dados,
        //Então tem que ser Feito assim:
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        
        //Aqui ele checa se os Dados não tão vazios, por motivos de vai que
        if (br != null) {
            
            //Converte os dados do JSON pra um Formato de Objeto que o Java consiga lidar
            json = br.readLine();
            byte[] bytes = json.getBytes(ISO_8859_1); 
            String jsonStr = new String(bytes, UTF_8);            
            JSONObject dados = new JSONObject(jsonStr);
            
            //Aqui, ele Instancia um objeto do Model endereco, e Popula ele com os dados do JSON
            Endereco endereco = new Endereco();
            endereco.setBairro(dados.getJSONObject("endereco").getString("bairro"));
            endereco.setCidade(dados.getJSONObject("endereco").getString("cidade"));
            endereco.setEstado(dados.getJSONObject("endereco").getString("estado"));
            endereco.setComplemento(dados.getJSONObject("endereco").getString("complemento"));
            endereco.setRua(dados.getJSONObject("endereco").getString("rua"));
            endereco.setNumero(dados.getJSONObject("endereco").getInt("numero"));
            
            //Aqui, ele Instancia um objeto do Model Cliente, e Popula ele com os dados do JSON
            Cliente cliente = new Cliente();
            cliente.setNome(dados.getJSONObject("usuario").getString("nome"));
            cliente.setSobrenome(dados.getJSONObject("usuario").getString("sobrenome"));
            cliente.setTelefone(dados.getJSONObject("usuario").getString("telefone"));
            cliente.setUsuario(dados.getJSONObject("usuario").getString("usuario"));
            cliente.setSenha(dados.getJSONObject("usuario").getString("senha"));
            cliente.setFg_ativo(1);
            
            //E Para finalizar, salva no Banco usando o DAO deles
            cliente.setEndereco(endereco);
            
            // Usa o daoCliente injetado se disponível, senão cria uma nova instância
            if (daoCliente != null) {
                daoCliente.salvar(cliente);
            } else {
                DaoCliente clienteDAO = new DaoCliente();
                clienteDAO.salvar(cliente);
            }
            
        }
        
        
        
        try (PrintWriter out = response.getWriter()) {
            
            //Aqui é onde a Resposta é mandada para o Cliente, dando um Feedback de que tudo deu certo.
            out.println("Usuário Cadastrado!");

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
