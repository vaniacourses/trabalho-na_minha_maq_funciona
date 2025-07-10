package unitTests;

import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;
    private String url = "http://localhost:8080/view/login/login.html"; 


    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getUrlAtual() {
        return driver.getCurrentUrl();
    }

    public boolean taNoLogin() {
        return getUrlAtual().equals(url);
    }
}