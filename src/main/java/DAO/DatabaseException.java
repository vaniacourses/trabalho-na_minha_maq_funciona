package DAO;

/**
 * Exceção personalizada para erros relacionados ao banco de dados
 * 
 * @author kener_000
 */
public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DatabaseException(Throwable cause) {
        super(cause);
    }
} 
