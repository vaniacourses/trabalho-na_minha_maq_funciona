package unit;

import Controllers.getBebidas;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GetBebidasTest {
    
    @Test
    public void deveTerServletInfo() {
        // Arrange
        getBebidas controller = new getBebidas();
        
        // Act
        String servletInfo = controller.getServletInfo();
        
        // Assert
        assertNotNull(servletInfo);
        assertTrue(servletInfo.length() > 0);
    }
    
    @Test
    public void deveSerInstanciavel() {
        // Arrange & Act
        getBebidas controller = new getBebidas();
        
        // Assert
        assertNotNull(controller);
    }
} 