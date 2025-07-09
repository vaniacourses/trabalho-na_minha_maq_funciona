package seleniumTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginFuncionarioPage {
  private WebDriver driver;

    private By inputUsuario = By.id("loginInput");
    private By inputSenha = By.id("senhaInput");
    private By botaoEntrar = By.className("buttonSubmit");

    public LoginFuncionarioPage(WebDriver driver) {
        this.driver = driver;
    }

    public void preencherLogin(String usuario, String senha) {
        driver.findElement(inputUsuario).sendKeys(usuario);
        driver.findElement(inputSenha).sendKeys(senha);
    }

    public void clicarEntrar() {
        driver.findElement(botaoEntrar).click();
    }
}
