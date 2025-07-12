package unit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class ControllerTest {
    
    @Mock
    protected HttpServletRequest request;
    
    @Mock
    protected HttpServletResponse response;
    
    protected StringWriter stringWriter;
    protected PrintWriter printWriter;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Configurar response mock
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        
        // Configurar comportamento padrão do response
        org.mockito.Mockito.when(response.getWriter()).thenReturn(printWriter);
        org.mockito.Mockito.when(response.getCharacterEncoding()).thenReturn("UTF-8");
    }
    
    protected String getResponseContent() {
        printWriter.flush();
        return stringWriter.toString();
    }
    
    protected void assertResponseContains(String expectedContent) {
        String response = getResponseContent();
        org.junit.jupiter.api.Assertions.assertTrue(
            response.contains(expectedContent),
            "Response deve conter: " + expectedContent + "\nResponse atual: " + response
        );
    }
    
    protected void assertResponseStatus(int expectedStatus) {
        org.mockito.Mockito.verify(response).setStatus(expectedStatus);
    }
} 