package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CadastroClientePage {
    private WebDriver driver;
    private WebDriverWait wait;

    public CadastroClientePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo() {
        driver.get("http://localhost:8080/view/cadastro/cadastro.html");
    }

    public void preencherDadosUsuario(String nome, String sobrenome, String telefone, String usuario, String senha) {
        driver.findElement(By.name("nome")).sendKeys(nome);
        driver.findElement(By.name("sobrenome")).sendKeys(sobrenome);
        driver.findElement(By.name("telefone")).sendKeys(telefone);
        driver.findElement(By.name("usuario")).sendKeys(usuario);
        driver.findElement(By.name("senha")).sendKeys(senha);
    }

    public void preencherDadosEndereco(String rua, String numero, String bairro, String complemento, String cidade, String estado) {
        driver.findElement(By.name("rua")).sendKeys(rua);
        driver.findElement(By.name("numero")).sendKeys(numero);
        driver.findElement(By.name("bairro")).sendKeys(bairro);
        driver.findElement(By.name("complemento")).sendKeys(complemento);
        driver.findElement(By.name("cidade")).sendKeys(cidade);
        driver.findElement(By.name("estado")).sendKeys(estado);
    }

    public void clicarCadastrar() {
        driver.findElement(By.cssSelector("button[onclick='enviarCadastro()']")).click();
    }
} 