package Controllers; 

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver; 
import org.openqa.selenium.support.ui.ExpectedConditions; 
import org.openqa.selenium.support.ui.WebDriverWait; 
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CadastroClienteTest {

    private WebDriver driver; 
    private CadastroClientePage cadastroPage; 
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {

        driver = new ChromeDriver(); 
        driver.manage().window().maximize(); 
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));


        cadastroPage = new CadastroClientePage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    void testCadastroClienteComSucesso() {
        cadastroPage.navigateTo(); 
        cadastroPage.preencherDadosUsuario("Cliente", "Sobrenome Teste", "21987654321", "novousuarioteste", "senhadiff123");
        cadastroPage.preencherDadosEndereco("Rua Teste", "123", "Bairro Selenium", "Casa 20", "Cidade do Teste", "RJ");
    
        cadastroPage.clicarCadastrar(); 


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        String alertMessage = driver.switchTo().alert().getText(); 
        driver.switchTo().alert().accept(); 

        assertTrue(alertMessage.contains("Usuário Cadastrado!"), "Mensagem de sucesso não apareceu no alert.");
        assertTrue(loginPage.taNoLogin(), "Não foi redirecionado para a página de login.");
    }

    @AfterEach
    void fechaTudo() {
        if (driver != null) {
            driver.quit(); 
        }
    }
}