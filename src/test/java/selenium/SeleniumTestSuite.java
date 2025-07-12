package selenium;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

/**
 * Suite de testes Selenium para testes de sistema
 * IMPORTANTE: Estes testes precisam do servidor rodando em localhost:8080
 * Para executar: mvn test -Dtest=SeleniumTestSuite -Dselenium.enabled=true
 */
@Tag("selenium")
@Tag("system")
public class SeleniumTestSuite extends BaseSeleniumTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    @Test
    public void testeFluxoCompletoCadastroCliente() {
        // 1. Acessar página de cadastro
        driver.get(BASE_URL + "/view/cadastro/cadastro.html");
        waitForPageLoad();
        
        // 2. Preencher formulário
        // 3. Submeter cadastro
        // 4. Verificar redirecionamento
        // 5. Fazer login
        // 6. Acessar menu
        // 7. Fazer pedido
        // 8. Verificar carrinho
    }
    
    @Test
    public void testeFluxoFuncionario() {
        // 1. Login funcionário
        // 2. Acessar painel
        // 3. Cadastrar ingrediente
        // 4. Cadastrar bebida
        // 5. Cadastrar lanche
        // 6. Verificar relatórios
    }
    
    @Test
    public void testeResponsividade() {
        // Testar em diferentes resoluções
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667)); // Mobile
        driver.get(BASE_URL);
        waitForPageLoad();
        
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080)); // Desktop
        driver.get(BASE_URL);
        waitForPageLoad();
    }
} 