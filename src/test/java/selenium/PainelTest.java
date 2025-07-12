package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Alert;
import pages.LoginFuncionarioPage;
import pages.PainelPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("selenium")
public class PainelTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    protected LoginFuncionarioPage loginPage;
    protected PainelPage painelPage;

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-save-password-bubble", "--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        loginPage = new LoginFuncionarioPage(driver);
        painelPage = new PainelPage(driver);
    }

    @BeforeEach
    void limparAlertasAntesDeCadaTeste() {
        limparAlertas();
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Teste básico de login de funcionário")
    void testLoginBasico() {
        driver.get("http://localhost:8080/view/login/login_Funcionario.html");
        
        wait.until(ExpectedConditions.urlContains("login_Funcionario.html"));
        
        // Verifica se os campos estão presentes
        assertTrue(driver.findElement(By.id("loginInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("senhaInput")).isDisplayed());
        
        // Captura logs do console antes do login
        System.out.println("=== Logs do console antes do login ===");
        driver.manage().logs().get("browser").forEach(log -> System.out.println(log.getMessage()));
        
        // Preenche o login
        driver.findElement(By.id("loginInput")).sendKeys("admin");
        driver.findElement(By.id("senhaInput")).sendKeys("admin123");
        
        // Clica no botão de login
        driver.findElement(By.cssSelector("button[onclick='enviarLoginFuncionario()']")).click();
        
        // Aguarda um pouco para ver se há redirecionamento
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Captura logs do console após o login
        System.out.println("=== Logs do console após o login ===");
        driver.manage().logs().get("browser").forEach(log -> System.out.println(log.getMessage()));
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("URL após tentativa de login: " + currentUrl);
        
        // Verifica se há alertas
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("Alerta encontrado: " + alertText);
            alert.accept();
        } catch (Exception e) {
            System.out.println("Nenhum alerta encontrado");
        }
    }

    @Test
    @DisplayName("Teste de cadastro de ingrediente na plataforma")
    void testCadastroIngrediente() throws InterruptedException {
        fazerLoginFuncionario();
        
        driver.get("http://localhost:8080/view/painel/painel.html");
        
        painelPage.acessarCadastroIngredientes();
        
        painelPage.cadastrarIngredienteCompleto(
            "Hambúrguer de Carne",
            "carne",
            50,
            8.50,
            12.00,
            "Hambúrguer de carne bovina premium"
        );
        
        String mensagemAlerta = painelPage.obterMensagemAlerta();
        
        assertTrue(mensagemAlerta.contains("Ingrediente Salvo!"), 
            "Deveria mostrar mensagem de sucesso, mas mostrou: " + mensagemAlerta);
    }

    protected void fazerLoginFuncionario() {
        limparAlertas();
        
        driver.get("http://localhost:8080/view/login/login_Funcionario.html");
        
        wait.until(ExpectedConditions.urlContains("login_Funcionario.html"));
        
        loginPage.preencherLogin("admin", "admin123");
        
        loginPage.clicarEntrar();
        
        // Aguarda o redirecionamento para o painel
        // Primeiro aguarda até 15 segundos para o redirecionamento
        try {
            wait.withTimeout(Duration.ofSeconds(15)).until(ExpectedConditions.urlContains("painel"));
        } catch (Exception e) {
            // Se não redirecionou, verifica se há algum erro
            String currentUrl = driver.getCurrentUrl();
            System.out.println("URL atual após login: " + currentUrl);
            
            // Verifica se há alertas de erro
            try {
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                System.out.println("Alerta encontrado: " + alertText);
                alert.accept();
                fail("Login falhou: " + alertText);
            } catch (Exception alertException) {
                // Se não há alerta, tenta navegar diretamente para o painel
                driver.get("http://localhost:8080/view/painel/painel.html");
                wait.until(ExpectedConditions.urlContains("painel"));
            }
        }
    }

    protected void limparAlertas() {
        try {
            while (true) {
                Alert alert = driver.switchTo().alert();
                alert.accept();
            }
        } catch (Exception e) {
        }
    }
}
