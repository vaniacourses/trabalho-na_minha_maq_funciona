package unitTests; 

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select; 

public class CadastroClientePage {

    private WebDriver driver; 
    private String url = "http://localhost:8080/view/cadastro/cadastro.html"; 


    By nomeInput = By.id("Espaçamentotitle"); 
    By sobrenomeInput = By.name("sobrenome"); 
    By telefoneInput = By.name("telefone");
    By usuarioInput = By.name("usuario"); 
    By senhaInput = By.name("senha"); 

    By ruaInput = By.name("rua");
    By numeroInput = By.name("numero");
    By bairroInput = By.name("bairro"); 
    By complementoInput = By.name("complemento");
    By cidadeInput = By.name("cidade"); 
    By estadoSelect = By.id("UF"); 

    By cadastrarButton = By.cssSelector("button.buttonSubmit"); 

    
    public CadastroClientePage(WebDriver driver){
        this.driver = driver;
    }


    public void navigateTo() {
        driver.get(url);
    }

    public void preencherDadosUsuario(String nome, String sobrenome, String telefone, String usuario, String senha) {
        driver.findElement(nomeInput).sendKeys(nome);
        driver.findElement(sobrenomeInput).sendKeys(sobrenome);
        driver.findElement(telefoneInput).sendKeys(telefone);
        driver.findElement(usuarioInput).sendKeys(usuario);
        driver.findElement(senhaInput).sendKeys(senha);
    }

    public void preencherDadosEndereco(String rua, String numero, String bairro, String complemento, String cidade, String estado) {
        driver.findElement(ruaInput).sendKeys(rua);
        driver.findElement(numeroInput).sendKeys(numero);
        driver.findElement(bairroInput).sendKeys(bairro);
        driver.findElement(complementoInput).sendKeys(complemento);
        driver.findElement(cidadeInput).sendKeys(cidade);
        
        Select estadoDropdown = new Select(driver.findElement(estadoSelect));
        estadoDropdown.selectByValue(estado);
    }

    public void clicarCadastrar() {
        driver.findElement(cadastrarButton).click();
    }
}