package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;
import java.time.Duration;

public class PainelPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public PainelPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void acessarCadastroIngredientes() {
        driver.findElement(By.cssSelector("button[onclick='abrirCadastroIngrediente()']")).click();
    }

    public void cadastrarIngredienteCompleto(String nome, String tipo, int quantidade, double valorCompra, double valorVenda, String descricao) {
        driver.findElement(By.id("nomeIngrediente")).sendKeys(nome);
        driver.findElement(By.id("tipoIngrediente")).sendKeys(tipo);
        driver.findElement(By.id("quantidadeIngrediente")).sendKeys(String.valueOf(quantidade));
        driver.findElement(By.id("valorCompraIngrediente")).sendKeys(String.valueOf(valorCompra));
        driver.findElement(By.id("valorVendaIngrediente")).sendKeys(String.valueOf(valorVenda));
        driver.findElement(By.id("descricaoIngrediente")).sendKeys(descricao);
        
        driver.findElement(By.cssSelector("button[onclick='salvarIngrediente()']")).click();
    }

    public String obterMensagemAlerta() {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String mensagem = alert.getText();
        alert.accept();
        return mensagem;
    }
} 