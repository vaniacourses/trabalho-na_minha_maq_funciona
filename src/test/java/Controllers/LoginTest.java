package Controllers;

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

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("selenium")
public class LoginTest {

    private static WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-save-password-bubble", "--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void deveLogarComSucesso() throws InterruptedException {
        driver.get("http://localhost:8080/view/login/login_Funcionario.html");

        LoginFuncionarioPage loginPage = new LoginFuncionarioPage(driver);
        loginPage.preencherLogin("admin", "admin123");
        loginPage.clicarEntrar();

        Thread.sleep(3000); 
        Assertions.assertTrue(driver.getCurrentUrl().contains("painel.html"));
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }
}
