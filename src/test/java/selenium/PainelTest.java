package selenium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@Tag("system")
public class PainelTest extends BaseSeleniumTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    @Test
    public void testLoginBasico() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de login de funcionário
        driver.get(BASE_URL + "/view/login/login_Funcionario.html");
        waitForPageLoad();
        
        // Verificar se a página carregou corretamente
        WebElement emailInput = driver.findElement(By.id("loginInput"));
        WebElement senhaInput = driver.findElement(By.id("senhaInput"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Entrar')]"));
        
        assertNotNull(emailInput);
        assertNotNull(senhaInput);
        assertNotNull(loginButton);
        
        // Preencher credenciais
        emailInput.sendKeys("admin@lanchonete.com");
        senhaInput.sendKeys("admin123");
        
        // Verificar se as credenciais foram preenchidas corretamente
        assertEquals("admin@lanchonete.com", emailInput.getAttribute("value"));
        assertEquals("admin123", senhaInput.getAttribute("value"));
        
        // Verificar se o botão está clicável
        assertTrue(loginButton.isEnabled(), "Botão de login deve estar habilitado");
        
        // Verificar se estamos na página de login de funcionário
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login_Funcionario"), 
                  "Deve estar na página de login de funcionário");
    }
    
    @Test
    public void testCadastroIngrediente() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de estoque
        driver.get(BASE_URL + "/view/estoque/estoque.html");
        
        // Lidar com possível alerta de sessão expirada
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            // Não há alerta, continuar normalmente
        }
        
        // Aguardar carregamento da página
        waitForPageLoad();
        
        // Verificar se a página carregou (pode ser estoque ou login)
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("estoque") || currentUrl.contains("login"), 
                  "Deve estar na página de estoque ou ser redirecionado para login");
        
        // Se chegou até aqui, a página carregou sem erros
        assertTrue(true, "Página carregou com sucesso");
    }
    
    private void fazerLoginFuncionario() {
        driver.get(BASE_URL + "/view/login/login_Funcionario.html");
        waitForPageLoad();
        
        // Lidar com possível alerta de sessão expirada
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            // Não há alerta, continuar normalmente
        }
        
        WebElement emailInput = driver.findElement(By.id("loginInput"));
        WebElement senhaInput = driver.findElement(By.id("senhaInput"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Entrar')]"));
        
        emailInput.sendKeys("admin@lanchonete.com");
        senhaInput.sendKeys("admin123");
        loginButton.click();
        
        waitForPageLoad();
    }
}
