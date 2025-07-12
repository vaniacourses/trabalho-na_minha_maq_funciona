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
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[onclick='showCadIngredienteDiv()']")));
        driver.findElement(By.cssSelector("button[onclick='showCadIngredienteDiv()']")).click();
    }

    public void cadastrarIngredienteCompleto(String nome, String tipo, int quantidade, double valorCompra, double valorVenda, String descricao) {
        // Aguardar o formulário estar visível
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='nome']")));
        
        // Preencher os campos usando os atributos name
        driver.findElement(By.cssSelector("input[name='nome']")).sendKeys(nome);
        driver.findElement(By.cssSelector("select[name='tipo']")).sendKeys(tipo);
        driver.findElement(By.cssSelector("input[name='quantidade']")).sendKeys(String.valueOf(quantidade));
        driver.findElement(By.cssSelector("input[name='ValorCompra']")).sendKeys(String.valueOf(valorCompra));
        driver.findElement(By.cssSelector("input[name='ValorVenda']")).sendKeys(String.valueOf(valorVenda));
        driver.findElement(By.cssSelector("textarea[name='descricao']")).sendKeys(descricao);
        
        // Clicar no botão salvar
        driver.findElement(By.cssSelector("input[onclick='salvarIngrediente()']")).click();
    }

    public String obterMensagemAlerta() {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String mensagem = alert.getText();
        alert.accept();
        return mensagem;
    }
} 