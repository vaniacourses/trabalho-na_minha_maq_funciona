package Helpers;

import DAO.DaoToken;
import jakarta.servlet.http.Cookie;

/**
 *
 * @author kener_000
 */
public class ValidadorCookie {
    
    private final DaoToken tokenDAO;

    /**
     * Construtor padrão para a aplicação.
     * A sua aplicação continuará a usar `new ValidadorCookie()` sem problemas.
     */
    public ValidadorCookie() {
        this.tokenDAO = new DaoToken();
    }

    /**
     * Construtor para testes.
     * Permite que os testes unitários "injetem" um DaoToken falso (mock).
     * @param tokenDAO O DAO a ser usado pela instância.
     */
    public ValidadorCookie(DaoToken tokenDAO) {
        this.tokenDAO = tokenDAO;
    }
    
    public boolean validar(Cookie[] cookies){
        if (cookies == null) {
            return false;
        }
        boolean resultado = false;
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals("token")){
                resultado = this.tokenDAO.validar(value);
            }
        }
        
        return resultado;
    }
    
    public boolean validarFuncionario(Cookie[] cookies){
        if (cookies == null) {
            return false;
        }
        
        boolean resultado = false;
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals("tokenFuncionario")){
                resultado = this.tokenDAO.validar(value);
            }
        }
        
        return resultado;
    }
        
    public void deletar(Cookie[] cookies){
        if (cookies == null) {
            return;
        }
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            try{
                if(name.equals("tokenFuncionario") || name.equals("token")){
                    this.tokenDAO.remover(value);
                }
            } catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    
    public String getCookieIdCliente(Cookie[] cookies){
        if (cookies == null) {
            return "erro";
        }
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals("token")){
                String[] palavras;
                palavras = value.split("-");
                return palavras[0];
            }
        }
        return "erro";
    }
    
    public String getCookieIdFuncionario(Cookie[] cookies){
        if (cookies == null) {
            return "erro";
        }
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals("tokenFuncionario")){
                String[] palavras;
                palavras = value.split("-");
                return palavras[0];
            }
        }
        return "erro";
    }
}
