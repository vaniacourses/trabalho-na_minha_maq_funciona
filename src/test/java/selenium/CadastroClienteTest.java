package selenium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@Tag("system")
public class CadastroClienteTest extends BaseSeleniumTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    @Test
    @Disabled("Servidor precisa estar rodando em localhost:8080")
    public void testCadastroClienteComSucesso() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de cadastro
        driver.get(BASE_URL + "/view/cadastro/cadastro.html");
        waitForPageLoad();
        
        // Verificar se a página carregou corretamente
        WebElement nomeInput = driver.findElement(By.id("nome"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement confirmarSenhaInput = driver.findElement(By.id("confirmarSenha"));
        WebElement cadastrarButton = driver.findElement(By.id("btnCadastrar"));
        
        assertNotNull(nomeInput);
        assertNotNull(emailInput);
        assertNotNull(senhaInput);
        assertNotNull(confirmarSenhaInput);
        assertNotNull(cadastrarButton);
        
        // Preencher dados do cliente
        nomeInput.sendKeys("João Silva");
        emailInput.sendKeys("joao.silva@teste.com");
        senhaInput.sendKeys("senha123");
        confirmarSenhaInput.sendKeys("senha123");
        
        // Clicar no botão de cadastrar
        cadastrarButton.click();
        
        // Aguardar processamento
        waitForPageLoad();
        
        // Verificar se foi redirecionado para login ou menu
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login") || currentUrl.contains("menu"), 
                  "Deve ser redirecionado após cadastro bem-sucedido");
    }
}