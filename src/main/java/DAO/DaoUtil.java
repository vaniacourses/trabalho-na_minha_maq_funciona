/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;


public class DaoUtil {
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar o driver do PostgreSQL", e);
        }
    }
    
    public Connection conecta(){
            try{
            String host = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            String port = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "5432";
            String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "lanchonete";
            String usuario = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
            String senha = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "postgres";
            
            String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
            return DriverManager.getConnection(url, usuario, senha);
            
        }catch(Exception e){
            throw new RuntimeException("Erro na base de dados :"+e.getMessage());
        }
    }
    
}
