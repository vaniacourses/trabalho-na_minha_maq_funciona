package selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseSeleniumTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    @Test
    public void deveLogarComSucesso() {
        // Navegar para a página de login
        driver.get(BASE_URL + "/view/login/login.html");
        waitForPageLoad();
        
        // Preencher credenciais
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        
        emailInput.sendKeys("teste@teste.com");
        senhaInput.sendKeys("123456");
        
        // Clicar no botão de login
        loginButton.click();
        
        // Aguardar e verificar redirecionamento
        waitForPageLoad();
        handleAlerts(); // Tratar possíveis alertas
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL atual após login: " + currentUrl);
        
        // Verificar se foi redirecionado para a página correta
        assertTrue(currentUrl.contains("/view/menu/menu.html") || 
                  currentUrl.contains("/view/home/home.html") ||
                  currentUrl.contains("/view/carrinho/carrinho.html"),
                  "Deve ser redirecionado para uma página válida após login");
    }
    
    @Test
    public void deveFalharComCredenciaisInvalidas() {
        // Navegar para a página de login
        driver.get(BASE_URL + "/view/login/login.html");
        waitForPageLoad();
        
        // Preencher credenciais inválidas
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        
        emailInput.sendKeys("invalido@teste.com");
        senhaInput.sendKeys("senhaerrada");
        
        // Clicar no botão de login
        loginButton.click();
        
        // Aguardar e verificar que permanece na página de login
        waitForPageLoad();
        handleAlerts();
        
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login.html"), 
                  "Deve permanecer na página de login com credenciais inválidas");
    }
}
