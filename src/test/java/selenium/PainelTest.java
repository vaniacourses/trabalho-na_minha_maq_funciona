package selenium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@Tag("system")
public class PainelTest extends BaseSeleniumTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    @Test
    @Disabled("Servidor precisa estar rodando em localhost:8080")
    public void testLoginBasico() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de login de funcionário
        driver.get(BASE_URL + "/view/login/login_Funcionario.html");
        waitForPageLoad();
        
        // Verificar se a página carregou corretamente
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        
        assertNotNull(emailInput);
        assertNotNull(senhaInput);
        assertNotNull(loginButton);
        
        // Preencher credenciais
        emailInput.sendKeys("admin@lanchonete.com");
        senhaInput.sendKeys("admin123");
        
        // Clicar no botão de login
        loginButton.click();
        
        // Aguardar redirecionamento
        waitForPageLoad();
        
        // Verificar se foi redirecionado para o painel
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("painel"), 
                  "Deve ser redirecionado para o painel após login");
    }
    
    @Test
    @Disabled("Servidor precisa estar rodando em localhost:8080")
    public void testCadastroIngrediente() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Fazer login primeiro
        fazerLoginFuncionario();
        
        // Navegar para a página de estoque
        driver.get(BASE_URL + "/view/estoque/estoque.html");
        waitForPageLoad();
        
        // Verificar se a página de estoque carregou
        WebElement adicionarButton = driver.findElement(By.id("btnAdicionarIngrediente"));
        assertNotNull(adicionarButton);
        
        // Clicar no botão de adicionar ingrediente
        adicionarButton.click();
        
        // Aguardar modal ou formulário aparecer
        waitForElement(By.id("nomeIngrediente"));
        
        // Preencher dados do ingrediente
        WebElement nomeInput = driver.findElement(By.id("nomeIngrediente"));
        WebElement precoInput = driver.findElement(By.id("precoIngrediente"));
        WebElement salvarButton = driver.findElement(By.id("btnSalvarIngrediente"));
        
        nomeInput.sendKeys("Queijo Cheddar");
        precoInput.sendKeys("5.50");
        
        // Salvar ingrediente
        salvarButton.click();
        
        // Aguardar confirmação
        waitForPageLoad();
        
        // Verificar se o ingrediente foi adicionado
        WebElement ingredienteAdicionado = driver.findElement(By.xpath("//td[contains(text(), 'Queijo Cheddar')]"));
        assertNotNull(ingredienteAdicionado);
    }
    
    private void fazerLoginFuncionario() {
        driver.get(BASE_URL + "/view/login/login_Funcionario.html");
        waitForPageLoad();
        
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        
        emailInput.sendKeys("admin@lanchonete.com");
        senhaInput.sendKeys("admin123");
        loginButton.click();
        
        waitForPageLoad();
    }
}
