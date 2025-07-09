package Controllers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PainelPage {
    private WebDriver driver;

    
    private By botaoAbrir = By.xpath("//button[contains(text(),'Abrir')]");
    private By botaoFechar = By.xpath("//button[contains(text(),'Fechar')]");
    
    private By tituloPainel = By.xpath("//h1[@class='titlePage']");

    private By statusLanchonete = By.cssSelector(".StatusLanchonete .legendStatus");

    public PainelPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clicarBotaoAbrir() {
        driver.findElement(botaoAbrir).click();
    }

    public void clicarBotaoFechar() {
        driver.findElement(botaoFechar).click();
    }

    public String getTituloPainel() {
        return driver.findElement(tituloPainel).getText().trim();
    }

    public String getStatusLanchonete() {
        WebElement status = driver.findElement(statusLanchonete);
        return status.getText().trim();
    }
}