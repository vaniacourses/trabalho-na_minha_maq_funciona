package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseSeleniumTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Configurar para aceitar alertas automaticamente
        driver.switchTo().alert().accept();
    }
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                // Tentar aceitar qualquer alerta antes de fechar
                driver.switchTo().alert().accept();
            } catch (Exception e) {
                // Ignorar se não houver alerta
            }
            driver.quit();
        }
    }
    
    protected void handleAlerts() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            // Não há alerta para aceitar
        }
    }
    
    protected void waitForElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    protected void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    protected void waitForPageLoad() {
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
} 