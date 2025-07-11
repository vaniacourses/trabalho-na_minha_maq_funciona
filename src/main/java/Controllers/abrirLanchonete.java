package Controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AbrirLanchonete", urlPatterns = {"/abrirLanchonete"})
public class abrirLanchonete extends HttpServlet {

    protected void processRequest(HttpServletResponse response)
            throws IOException {
        
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("Lanchonete aberta com sucesso!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para abrir a lanchonete";
    }
} 
