package Controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.time.Duration;

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
