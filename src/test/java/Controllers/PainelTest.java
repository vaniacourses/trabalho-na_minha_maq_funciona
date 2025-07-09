package Controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PainelTest {

    private WebDriver driver;

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-save-password-bubble", "--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    void deveAbrirLanchoneteComSucesso() throws InterruptedException {
        driver.get("http://localhost:8080/view/painel/painel.html");

        PainelPage painelPage = new PainelPage(driver);
        painelPage.clicarBotaoAbrir();

        Thread.sleep(2000); // aguarda atualização
        assertEquals("Aberto agora!", painelPage.getStatusLanchonete());
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }
}
