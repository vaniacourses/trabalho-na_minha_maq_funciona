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
    @Disabled("Servidor precisa estar rodando em localhost:8080")
    public void deveLogarComSucesso() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
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
        
        // Aguardar redirecionamento ou verificar resultado
        waitForPageLoad();
        
        // Verificar se foi redirecionado para a página principal
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("menu") || currentUrl.contains("home"), 
                  "Deve ser redirecionado após login bem-sucedido");
    }
    
    @Test
    @Disabled("Servidor precisa estar rodando em localhost:8080")
    public void deveFalharComCredenciaisInvalidas() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
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
        
        // Aguardar e verificar se permanece na página de login
        waitForPageLoad();
        
        // Verificar se ainda está na página de login (não foi redirecionado)
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login"), 
                  "Deve permanecer na página de login com credenciais inválidas");
    }
}
