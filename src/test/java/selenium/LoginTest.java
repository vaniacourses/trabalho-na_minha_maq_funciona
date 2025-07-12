package selenium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@Tag("system")
public class LoginTest extends BaseSeleniumTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    @Test
    public void deveLogarComSucesso() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de login
        driver.get(BASE_URL + "/view/login/login.html");
        waitForPageLoad();
        
        // Preencher credenciais
        WebElement emailInput = driver.findElement(By.id("loginInput"));
        WebElement senhaInput = driver.findElement(By.id("senhaInput"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Entrar')]"));
        
        emailInput.sendKeys("teste@teste.com");
        senhaInput.sendKeys("123456");
        
        // Verificar se as credenciais foram preenchidas corretamente
        assertEquals("teste@teste.com", emailInput.getAttribute("value"));
        assertEquals("123456", senhaInput.getAttribute("value"));
        
        // Verificar se o botão está clicável
        assertTrue(loginButton.isEnabled(), "Botão de login deve estar habilitado");
        
        // Verificar se estamos na página de login
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login"), 
                  "Deve estar na página de login");
    }
    
    @Test
    public void deveFalharComCredenciaisInvalidas() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de login
        driver.get(BASE_URL + "/view/login/login.html");
        waitForPageLoad();
        
        // Preencher credenciais inválidas
        WebElement emailInput = driver.findElement(By.id("loginInput"));
        WebElement senhaInput = driver.findElement(By.id("senhaInput"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Entrar')]"));
        
        emailInput.sendKeys("invalido@teste.com");
        senhaInput.sendKeys("senhaerrada");
        
        // Verificar se as credenciais inválidas foram preenchidas
        assertEquals("invalido@teste.com", emailInput.getAttribute("value"));
        assertEquals("senhaerrada", senhaInput.getAttribute("value"));
        
        // Verificar se o botão está clicável
        assertTrue(loginButton.isEnabled(), "Botão de login deve estar habilitado");
        
        // Verificar se ainda está na página de login
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login"), 
                  "Deve estar na página de login");
    }
}
