package Controllers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class PainelPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By botaoAbrir = By.xpath("//button[contains(text(),'Abrir')]");
    private By botaoFechar = By.xpath("//button[contains(text(),'Fechar')]");
    private By tituloPainel = By.xpath("//h1[@class='titlePage']");
    private By statusLanchonete = By.cssSelector(".StatusLanchonete .legendStatus");

    public PainelPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clicarBotaoAbrir() {
        try {
            driver.findElement(botaoAbrir).click();
        } catch (Exception e) {
            // Se houver um alerta, aceita ele
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
                // Tenta clicar novamente
                driver.findElement(botaoAbrir).click();
            } catch (Exception ex) {
                throw e;
            }
        }
    }

    public void clicarBotaoFechar() {
        try {
            driver.findElement(botaoFechar).click();
        } catch (Exception e) {
            // Se houver um alerta, aceita ele
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
                // Tenta clicar novamente
                driver.findElement(botaoFechar).click();
            } catch (Exception ex) {
                throw e;
            }
        }
    }

    public String getTituloPainel() {
        return driver.findElement(tituloPainel).getText().trim();
    }

    public String getStatusLanchonete() {
        WebElement status = driver.findElement(statusLanchonete);
        return status.getText().trim();
    }
}