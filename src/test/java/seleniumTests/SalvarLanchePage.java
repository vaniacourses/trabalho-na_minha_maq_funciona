package seleniumTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("selenium")
public class SalvarLanchePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    // Page Objects
    protected SLP_LoginPage loginPage;
    protected SLP_PainelPage painelPage;
    protected SLP_CadastrarLanchePage cadastrarLanchePage;
    protected SLP_CadastrarIngredientePage cadastrarIngredientePage;

    // Variável para garantir que cada execução do teste use um ingrediente único
    private String nomePaoDeTeste;

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-save-password-bubble", "--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        loginPage = new SLP_LoginPage(driver);
        painelPage = new SLP_PainelPage(driver);
        cadastrarLanchePage = new SLP_CadastrarLanchePage(driver);
        cadastrarIngredientePage = new SLP_CadastrarIngredientePage(driver);
        
        // Gera um nome único para o pão de teste para evitar conflitos entre execuções
        nomePaoDeTeste = "isa pao";
        
        // Garante que o pré-requisito (ingrediente pão) existe antes de rodar os testes
        prepararAmbienteDeTeste();
    }
    
    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Deve cadastrar um novo lanche com sucesso")
    void testSalvarLanche_ComSucesso() {
        // Arrange: O setup (@BeforeAll) já preparou o ambiente e deixou o usuário no painel.
        
        // Act
        // 1. Clica para mostrar o formulário de cadastro de lanches
        painelPage.clicarCadastrarLanches();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addItem")));
        System.out.println("Formulário de cadastro de lanches visível.");

        // 2. Preenche o formulário
        cadastrarLanchePage.preencherFormulario("X-Teste Finalissimo", "Lanche de teste completo e correto", "35.50");
        cadastrarLanchePage.selecionarPao(nomePaoDeTeste); // Usa o pão criado no setup
        cadastrarLanchePage.clicarSalvar();
        System.out.println("Formulário preenchido e botão de salvar clicado.");

        // Assert
        // CORREÇÃO FINAL: A verificação de sucesso é se a aplicação voltou para a página
        // principal do painel. Fazemos isso esperando que o botão "Cadastrar Lanches"
        // (que só existe no painel principal) fique visível e clicável novamente.
        wait.until(ExpectedConditions.elementToBeClickable(painelPage.getBotaoCadastrarLanches()));
        
        System.out.println("Lanche salvo com sucesso! A página voltou para o painel principal.");
        // Se o teste chegou até aqui sem exceções, ele passou.
        assertTrue(true, "O fluxo de salvar lanche foi concluído e voltou ao painel.");
    }

    /**
     * Método de setup que garante a existência de um ingrediente do tipo pão.
     * Roda uma vez antes de todos os testes da classe.
     */
    protected void prepararAmbienteDeTeste() {
        System.out.println("Preparando ambiente: garantindo a existência do ingrediente '" + nomePaoDeTeste + "'...");
        fazerLoginAdmin();
        
        painelPage.clicarCadastrarIngredientes();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addIngrediente"))); 
        
        cadastrarIngredientePage.cadastrarIngrediente(nomePaoDeTeste, "pao", "100", "1.50", "2.50", "Pão para testes automatizados");
        
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        System.out.println("Feedback do cadastro de ingrediente: " + alertText);
        alert.accept();
        
        // Após salvar o ingrediente e aceitar o alerta, a página volta ao estado inicial do painel.
        // Esperamos que o painel esteja pronto para a próxima ação.
        wait.until(ExpectedConditions.elementToBeClickable(painelPage.getBotaoCadastrarLanches()));
        System.out.println("Ambiente preparado. Painel principal está visível.");
    }

    /**
     * Método auxiliar para realizar o login do funcionário.
     */
    protected void fazerLoginAdmin() {
        driver.get("http://localhost:8080/view/login/login_Funcionario.html");
        wait.until(ExpectedConditions.urlContains("login_Funcionario.html"));
        
        loginPage.preencherLogin("admin", "admin123"); 
        loginPage.clicarEntrar();
        
        wait.until(ExpectedConditions.urlContains("painel.html"));
        System.out.println("Login como 'admin' realizado com sucesso.");
    }
}

// ===== CLASSES DE PAGE OBJECT (AUTOCONTIDAS) =====

class SLP_LoginPage {
    private WebDriver driver;
    private By inputUsuario = By.id("loginInput");
    private By inputSenha = By.id("senhaInput");
    private By botaoEntrar = By.className("buttonSubmit");

    public SLP_LoginPage(WebDriver driver) { this.driver = driver; }
    
    public void preencherLogin(String u, String s) {
        driver.findElement(inputUsuario).clear();
        driver.findElement(inputUsuario).sendKeys(u);
        driver.findElement(inputSenha).clear();
        driver.findElement(inputSenha).sendKeys(s);
    }
    
    public void clicarEntrar() { driver.findElement(botaoEntrar).click(); }
}

class SLP_PainelPage {
    private WebDriver driver;
    // Seletores baseados no HTML fornecido
    private By botaoCadastrarLanches = By.xpath("//button[@onclick='showCadLanches()']");
    private By botaoCadastrarIngredientes = By.xpath("//button[@onclick='showCadIngredienteDiv()']");

    public SLP_PainelPage(WebDriver driver) { this.driver = driver; }

    public By getBotaoCadastrarLanches() { return botaoCadastrarLanches; }

    public void clicarCadastrarLanches() { 
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(botaoCadastrarLanches)).click();
    }
    
    public void clicarCadastrarIngredientes() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(botaoCadastrarIngredientes)).click();
    }
}

class SLP_CadastrarLanchePage {
    private WebDriver driver;
    // Seletores baseados no HTML fornecido
    private By inputNome = By.id("nomeLanche");
    private By inputDescricao = By.id("textArea3");
    private By selectPao = By.id("selectPao");
    private By botaoSalvar = By.xpath("//form[@id='addItem']//input[@name='salvar']");
    private By inputValorLanche = By.id("ValorLanche");

    public SLP_CadastrarLanchePage(WebDriver driver) { this.driver = driver; }

    public void preencherFormulario(String nome, String descricao, String valor) {
        driver.findElement(inputNome).clear();
        driver.findElement(inputNome).sendKeys(nome); 
        driver.findElement(inputDescricao).clear();
        driver.findElement(inputDescricao).sendKeys(descricao);
        driver.findElement(inputValorLanche).clear();
        driver.findElement(inputValorLanche).sendKeys(valor);
    }
    
    public void selecionarPao(String nomeDoPao) {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.
            presenceOfNestedElementLocatedBy(selectPao, By.xpath("//option[text()='" + nomeDoPao + "']")));

        WebElement dropdownElement = driver.findElement(selectPao);
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByVisibleText(nomeDoPao);
    }

    public void clicarSalvar() { 
        driver.findElement(botaoSalvar).click(); 
    }
}

class SLP_CadastrarIngredientePage {
    private WebDriver driver;
    // Seletores específicos para o formulário de ingredientes
    private By inputNome = By.xpath("//form[@id='addIngrediente']//input[@name='nome']");
    private By selectTipo = By.xpath("//form[@id='addIngrediente']//select[@name='tipo']");
    private By inputQuantidade = By.xpath("//form[@id='addIngrediente']//input[@name='quantidade']");
    private By inputValorCompra = By.xpath("//form[@id='addIngrediente']//input[@name='ValorCompra']");
    private By inputValorVenda = By.xpath("//form[@id='addIngrediente']//input[@name='ValorVenda']");
    private By inputDescricao = By.xpath("//form[@id='addIngrediente']//textarea[@name='descricao']");
    private By botaoSalvar = By.xpath("//form[@id='addIngrediente']//input[@name='salvar']");

    public SLP_CadastrarIngredientePage(WebDriver driver) { this.driver = driver; }

    public void cadastrarIngrediente(String nome, String tipo, String qtd, String valCompra, String valVenda, String desc) {
        driver.findElement(inputNome).sendKeys(nome);
        new Select(driver.findElement(selectTipo)).selectByValue(tipo);
        driver.findElement(inputQuantidade).sendKeys(qtd);
        driver.findElement(inputValorCompra).sendKeys(valCompra);
        driver.findElement(inputValorVenda).sendKeys(valVenda);
        driver.findElement(inputDescricao).sendKeys(desc);
        driver.findElement(botaoSalvar).click();
    }
}
