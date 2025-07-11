package seleniumTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("selenium")
public class SalvarLanchePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    // Page Objects que serão usados pelos testes
    protected SLP_LoginPage loginPage;
    protected SLP_PainelPage painelPage;
    protected SLP_CadastrarLanchePage cadastrarLanchePage;

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();

        // Configura opções do Chrome para um ambiente de teste mais limpo
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-save-password-bubble", "--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        
        // Espera explícita é a melhor prática
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Inicializa os Page Objects uma vez para toda a classe
        loginPage = new SLP_LoginPage(driver);
        painelPage = new SLP_PainelPage(driver);
        cadastrarLanchePage = new SLP_CadastrarLanchePage(driver);
    }
    
    @BeforeEach
    void limparEstadoAntesDeCadaTeste() {
        // Como o navegador não é reiniciado, limpamos os alertas para evitar interferência entre testes.
        limparAlertas();
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
        // Arrange: Garante que o usuário está logado
        fazerLoginAdmin();
        
        // Navega para a página de cadastro de lanches a partir do painel
        painelPage.clicarCadastrarLanches();
        wait.until(ExpectedConditions.urlContains("cadastros/lanches.html"));
        System.out.println("Navegou para a página de cadastro de lanches.");

        // Act: Preenche o formulário com dados válidos e salva
        cadastrarLanchePage.preencherFormulario("X-Bacon Supremo", "Pão, hambúrguer, queijo e muito bacon", "32.50");
        cadastrarLanchePage.clicarSalvar();
        System.out.println("Formulário preenchido e botão de salvar clicado.");

        // Assert: Verifica se a mensagem de sucesso é exibida
        By seletorMensagemSucesso = By.id("mensagem-sucesso"); // Adapte o ID se necessário
        WebElement mensagemElement = wait.until(ExpectedConditions.visibilityOfElementLocated(seletorMensagemSucesso));
        
        String textoMensagem = mensagemElement.getText();
        System.out.println("Mensagem de feedback recebida: " + textoMensagem);

        assertTrue(mensagemElement.isDisplayed(), "A mensagem de sucesso deveria ser exibida.");
        assertTrue(textoMensagem.contains("Lanche Salvo com Sucesso!"), "O conteúdo da mensagem de sucesso está incorreto.");
    }

    @Test
    @DisplayName("Deve exibir erro ao tentar salvar lanche com nome em branco")
    void testSalvarLanche_ValidacaoNomeEmBranco() {
        // Arrange
        fazerLoginAdmin();
        
        // Navega para a página de cadastro através do painel
        painelPage.clicarCadastrarLanches();
        wait.until(ExpectedConditions.urlContains("cadastros/lanches.html"));
        
        // Act: Deixa o nome em branco e tenta salvar
        cadastrarLanchePage.preencherFormulario("", "Lanche sem nome para teste de validação", "10.00");
        cadastrarLanchePage.clicarSalvar();
        System.out.println("Tentativa de salvar lanche com nome em branco.");

        // Assert: Verifica se a mensagem de erro de validação é exibida
        By seletorMensagemErro = By.id("mensagem-erro-validacao"); // Adapte o ID se necessário
        WebElement mensagemElement = wait.until(ExpectedConditions.visibilityOfElementLocated(seletorMensagemErro));

        String textoMensagem = mensagemElement.getText();
        System.out.println("Mensagem de erro de validação recebida: " + textoMensagem);

        assertTrue(mensagemElement.isDisplayed(), "A mensagem de erro de validação deveria ser exibida.");
        assertEquals("O nome do lanche é obrigatório.", textoMensagem, "A mensagem de erro está incorreta.");
    }

    /**
     * Método auxiliar para realizar o login do funcionário.
     * Leva o sistema a um estado "logado".
     */
    protected void fazerLoginAdmin() {
        // Usando o usuário 'admin' que já existe, como no PainelTest.
        driver.get("http://localhost:8080/view/login/login_Funcionario.html");
        wait.until(ExpectedConditions.urlContains("login_Funcionario.html"));
        
        loginPage.preencherLogin("admin", "admin123"); 
        loginPage.clicarEntrar();
        
        // Aguarda o redirecionamento para o painel
        wait.until(ExpectedConditions.urlContains("painel.html"));
        System.out.println("Login como 'admin' realizado com sucesso.");
    }

    /**
     * Método auxiliar para limpar quaisquer alertas que possam ter ficado abertos,
     * evitando que um teste interfira no próximo.
     */
    protected void limparAlertas() {
        try {
            // Tenta aceitar um alerta se ele existir, com um timeout curto.
            Alert alert = new WebDriverWait(driver, Duration.ofSeconds(1)).until(ExpectedConditions.alertIsPresent());
            System.out.println("Alerta encontrado e limpo: " + alert.getText());
            alert.accept();
        } catch (Exception e) {
            // Nenhum alerta presente, o que é o esperado na maioria das vezes.
        }
    }
}

/**
 * Page Object para a página de Login do Funcionário.
 */
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

/**
 * Page Object para a página do Painel do Administrador.
 */
class SLP_PainelPage {
    private WebDriver driver;
    
    // CORREÇÃO: Alterado o seletor para buscar o link pelo seu texto visível,
    // que é uma abordagem mais robusta que depender de um ID específico.
    private By linkCadastrarLanches = By.linkText("Cadastrar Lanches"); 

    public SLP_PainelPage(WebDriver driver) { this.driver = driver; }

    public void clicarCadastrarLanches() { 
        driver.findElement(linkCadastrarLanches).click(); 
    }
}

/**
 * Page Object para a página de Cadastro de Lanches.
 */
class SLP_CadastrarLanchePage {
    private WebDriver driver;
    // Adapte os seletores para os IDs dos seus campos no HTML
    private By inputNome = By.id("nomeLanche");
    private By inputDescricao = By.id("descricaoLanche");
    private By inputValorVenda = By.id("valorVendaLanche");
    private By botaoSalvar = By.id("salvar-lanche-btn"); 

    public SLP_CadastrarLanchePage(WebDriver driver) { this.driver = driver; }

    public void preencherFormulario(String nome, String descricao, String valor) {
        driver.findElement(inputNome).clear();
        driver.findElement(inputNome).sendKeys(nome); 
        
        driver.findElement(inputDescricao).clear();
        driver.findElement(inputDescricao).sendKeys(descricao); 
        
        driver.findElement(inputValorVenda).clear();
        driver.findElement(inputValorVenda).sendKeys(valor); 
    }

    public void clicarSalvar() { 
        driver.findElement(botaoSalvar).click(); 
    }
}
