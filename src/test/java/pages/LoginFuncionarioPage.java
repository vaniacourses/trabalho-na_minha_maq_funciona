package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginFuncionarioPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public LoginFuncionarioPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void preencherLogin(String usuario, String senha) {
        driver.findElement(By.id("loginInput")).sendKeys(usuario);
        driver.findElement(By.id("senhaInput")).sendKeys(senha);
    }

    public void clicarEntrar() {
        driver.findElement(By.cssSelector("button[onclick='enviarLoginFuncionario()']")).click();
    }
} 