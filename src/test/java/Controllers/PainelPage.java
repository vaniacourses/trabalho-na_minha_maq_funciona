package Controllers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;

public class PainelPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By botaoAbrir = By.xpath("//button[contains(text(),'Abrir')]");
    private By botaoFechar = By.xpath("//button[contains(text(),'Fechar')]");
    private By tituloPainel = By.xpath("//h1[@class='titlePage']");
    private By statusLanchonete = By.cssSelector(".StatusLanchonete .legendStatus");

    private By botaoCadastrarIngredientes = By.xpath("//button[contains(text(),'Cadastrar Ingredientes')]");
    private By inputNomeIngrediente = By.cssSelector("input[name='nome']");
    private By selectTipoIngrediente = By.cssSelector("select[name='tipo']");
    private By inputQuantidadeIngrediente = By.cssSelector("input[name='quantidade']");
    private By inputValorCompraIngrediente = By.cssSelector("input[name='ValorCompra']");
    private By inputValorVendaIngrediente = By.cssSelector("input[name='ValorVenda']");
    private By textareaDescricaoIngrediente = By.cssSelector("textarea[name='descricao']");
    private By botaoSalvarIngrediente = By.cssSelector("input[value='Salvar']");
    private By botaoCancelarIngrediente = By.cssSelector("input[value='Cancelar']");
    private By formularioIngrediente = By.id("addIngrediente");

    public PainelPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clicarBotaoAbrir() {
        try {
            driver.findElement(botaoAbrir).click();
        } catch (Exception e) {
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
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
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
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

    public void acessarCadastroIngredientes() {
        WebElement botao = wait.until(
            ExpectedConditions.elementToBeClickable(botaoCadastrarIngredientes)
        );
        botao.click();
    }

    public void preencherNomeIngrediente(String nome) {
        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(inputNomeIngrediente)
        );
        input.sendKeys(nome);
    }

    public void selecionarTipoIngrediente(String tipo) {
        WebElement select = driver.findElement(selectTipoIngrediente);
        select.click();
        WebElement opcao = driver.findElement(By.cssSelector("option[value='" + tipo + "']"));
        opcao.click();
    }

    public void preencherQuantidadeIngrediente(int quantidade) {
        WebElement input = driver.findElement(inputQuantidadeIngrediente);
        input.sendKeys(String.valueOf(quantidade));
    }

    public void preencherValorCompraIngrediente(double valor) {
        WebElement input = driver.findElement(inputValorCompraIngrediente);
        input.clear();
        input.sendKeys(String.valueOf(valor));
    }

    public void preencherValorVendaIngrediente(double valor) {
        WebElement input = driver.findElement(inputValorVendaIngrediente);
        input.clear();
        input.sendKeys(String.valueOf(valor));
    }

    public void preencherDescricaoIngrediente(String descricao) {
        WebElement textarea = driver.findElement(textareaDescricaoIngrediente);
        textarea.sendKeys(descricao);
    }

    public void salvarIngrediente() {
        WebElement botao = driver.findElement(botaoSalvarIngrediente);
        botao.click();
    }

    public void cancelarCadastroIngrediente() {
        WebElement botao = driver.findElement(botaoCancelarIngrediente);
        botao.click();
    }

    public boolean formularioIngredienteEstaVisivel() {
        try {
            WebElement formulario = wait.until(
                ExpectedConditions.visibilityOfElementLocated(formularioIngrediente)
            );
            return formulario.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String obterMensagemAlerta() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String mensagem = alert.getText();
        alert.accept();
        return mensagem;
    }

    public void cadastrarIngredienteCompleto(String nome, String tipo, int quantidade, 
                                           double valorCompra, double valorVenda, String descricao) {
        preencherNomeIngrediente(nome);
        selecionarTipoIngrediente(tipo);
        preencherQuantidadeIngrediente(quantidade);
        preencherValorCompraIngrediente(valorCompra);
        preencherValorVendaIngrediente(valorVenda);
        preencherDescricaoIngrediente(descricao);
        salvarIngrediente();
    }
}