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
    public void testCadastroClienteComSucesso() {
        // Verificar se o servidor está rodando
        if (!isServerRunning()) {
            throw new RuntimeException("Servidor não está rodando em " + BASE_URL);
        }
        
        // Navegar para a página de cadastro
        driver.get(BASE_URL + "/view/cadastro/cadastro.html");
        waitForPageLoad();
        
        // Verificar se a página carregou corretamente
        WebElement nomeInput = driver.findElement(By.id("Espaçamentotitle"));
        WebElement sobrenomeInput = driver.findElement(By.name("sobrenome"));
        WebElement telefoneInput = driver.findElement(By.name("telefone"));
        WebElement usuarioInput = driver.findElement(By.name("usuario"));
        WebElement senhaInput = driver.findElement(By.name("senha"));
        WebElement cadastrarButton = driver.findElement(By.xpath("//button[contains(text(), 'Cadastrar')]"));
        
        assertNotNull(nomeInput);
        assertNotNull(sobrenomeInput);
        assertNotNull(telefoneInput);
        assertNotNull(usuarioInput);
        assertNotNull(senhaInput);
        assertNotNull(cadastrarButton);
        
        // Preencher dados do cliente
        nomeInput.sendKeys("João");
        sobrenomeInput.sendKeys("Silva");
        telefoneInput.sendKeys("11999999999");
        usuarioInput.sendKeys("joao.silva@teste.com");
        senhaInput.sendKeys("senha123");
        
        // Verificar se o formulário foi preenchido corretamente
        assertEquals("João", nomeInput.getAttribute("value"));
        assertEquals("joao.silva@teste.com", usuarioInput.getAttribute("value"));
        assertEquals("senha123", senhaInput.getAttribute("value"));
        
        // Verificar se o botão está clicável
        assertTrue(cadastrarButton.isEnabled(), "Botão de cadastrar deve estar habilitado");
        
        // Verificar se ainda estamos na página de cadastro
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("cadastro"), 
                  "Deve estar na página de cadastro");
    }
}