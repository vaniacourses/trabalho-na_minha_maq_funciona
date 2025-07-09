package Controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Alert;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void fazerLoginFuncionario() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
        }
        
        driver.get("http://localhost:8080/view/login/login_Funcionario.html");
        
        wait.until(ExpectedConditions.urlContains("login_Funcionario.html"));
        
        loginPage.preencherLogin("admin", "admin123");
        
        loginPage.clicarEntrar();
        
        wait.until(ExpectedConditions.urlContains("painel"));
    }

    protected void limparAlertas() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
        }
    }
}
