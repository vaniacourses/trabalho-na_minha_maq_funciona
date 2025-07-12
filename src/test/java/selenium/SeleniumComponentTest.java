package selenium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Selenium de componentes que não precisam do servidor
 * Estes testes verificam a estrutura HTML e comportamento básico
 */
@Tag("selenium")
@Tag("component")
public class SeleniumComponentTest extends BaseSeleniumTest {
    
    @Test
    public void deveTestarEstruturaHTMLBasica() {
        // Criar uma página HTML simples para teste
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Teste Componente</title>
            </head>
            <body>
                <form id="loginForm">
                    <input type="email" id="email" placeholder="Email">
                    <input type="password" id="senha" placeholder="Senha">
                    <button type="submit" id="btnLogin">Login</button>
                </form>
            </body>
            </html>
            """;
        
        // Navegar para uma página de dados
        driver.get("data:text/html;charset=utf-8," + htmlContent);
        waitForPageLoad();
        
        // Verificar se os elementos existem
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        
        assertNotNull(emailInput);
        assertNotNull(senhaInput);
        assertNotNull(loginButton);
        
        // Verificar se os elementos são interativos
        assertTrue(emailInput.isEnabled());
        assertTrue(senhaInput.isEnabled());
        assertTrue(loginButton.isEnabled());
        
        // Testar interação básica
        emailInput.sendKeys("teste@teste.com");
        senhaInput.sendKeys("123456");
        
        assertEquals("teste@teste.com", emailInput.getAttribute("value"));
        assertEquals("123456", senhaInput.getAttribute("value"));
    }
    
    @Test
    public void deveTestarFormularioCadastro() {
        // Criar página de cadastro para teste
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Cadastro</title>
            </head>
            <body>
                <form id="cadastroForm">
                    <input type="text" id="nome" placeholder="Nome">
                    <input type="email" id="email" placeholder="Email">
                    <input type="password" id="senha" placeholder="Senha">
                    <input type="password" id="confirmarSenha" placeholder="Confirmar Senha">
                    <button type="submit" id="btnCadastrar">Cadastrar</button>
                </form>
            </body>
            </html>
            """;
        
        driver.get("data:text/html;charset=utf-8," + htmlContent);
        waitForPageLoad();
        
        // Verificar estrutura do formulário
        WebElement nomeInput = driver.findElement(By.id("nome"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement confirmarSenhaInput = driver.findElement(By.id("confirmarSenha"));
        WebElement cadastrarButton = driver.findElement(By.id("btnCadastrar"));
        
        // Testar preenchimento
        nomeInput.sendKeys("João Silva");
        emailInput.sendKeys("joao@teste.com");
        senhaInput.sendKeys("senha123");
        confirmarSenhaInput.sendKeys("senha123");
        
        // Verificar valores
        assertEquals("João Silva", nomeInput.getAttribute("value"));
        assertEquals("joao@teste.com", emailInput.getAttribute("value"));
        assertEquals("senha123", senhaInput.getAttribute("value"));
        assertEquals("senha123", confirmarSenhaInput.getAttribute("value"));
    }
    
    @Test
    public void deveTestarValidacaoBasica() {
        // Criar página com validação HTML5
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Validação</title>
            </head>
            <body>
                <form id="validacaoForm">
                    <input type="email" id="email" required placeholder="Email">
                    <input type="password" id="senha" required minlength="6" placeholder="Senha">
                    <button type="submit" id="btnValidar">Validar</button>
                </form>
            </body>
            </html>
            """;
        
        driver.get("data:text/html;charset=utf-8," + htmlContent);
        waitForPageLoad();
        
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement senhaInput = driver.findElement(By.id("senha"));
        WebElement validarButton = driver.findElement(By.id("btnValidar"));
        
        // Verificar atributos de validação
        assertTrue(emailInput.getAttribute("required") != null);
        assertTrue(senhaInput.getAttribute("required") != null);
        assertEquals("6", senhaInput.getAttribute("minlength"));
        
        // Testar validação com dados inválidos
        emailInput.sendKeys("email-invalido");
        senhaInput.sendKeys("123");
        
        // Tentar submeter (deve falhar na validação HTML5)
        validarButton.click();
        
        // Verificar se os campos ainda têm os valores (formulário não foi submetido)
        assertEquals("email-invalido", emailInput.getAttribute("value"));
        assertEquals("123", senhaInput.getAttribute("value"));
    }
} 