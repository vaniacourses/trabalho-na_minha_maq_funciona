/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import dao.DaoToken;
import jakarta.servlet.http.Cookie;

/**
 *
 * @author kener_000
 */
public class ValidadorCookie {
    
    public boolean validar(Cookie[] cookies){
        if (cookies == null) {
            return false;
        }
        boolean resultado = false;
        DaoToken tokenDAO = new DaoToken();
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals("token")){
                resultado = tokenDAO.validar(value);
            }
        }
        
        return resultado;
    }
    
        public boolean validarFuncionario(Cookie[] cookies){
        if (cookies == null) {
            return false;
        }
        
        boolean resultado = false;
        DaoToken tokenDAO = new DaoToken();
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            if(name.equals("tokenFuncionario")){
                resultado = tokenDAO.validar(value);
            }
        }
        
        return resultado;
    }
        
    public void deletar(Cookie[] cookies){
        
        if (cookies == null) {
            return ;
        }
        DaoToken tokenDAO = new DaoToken();
        
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            String value = cookies[i].getValue();
            
            try{
            if(name.equals("tokenFuncionario")||name.equals("token")){
                tokenDAO.remover(value);
            }}catch(Exception e){
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
