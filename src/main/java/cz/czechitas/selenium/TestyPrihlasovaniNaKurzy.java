package cz.czechitas.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    WebDriver prohlizec;

    String url = "https://cz-test-jedna.herokuapp.com/";
    String uzivatel = "Jarda Carda";
    String uzivatelEmail = "cardousek@gmail.com";
    String uzivatelHeslo = "Skola2021";

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Test
    public void rodicJeSchopnySePrihlasit() {
        prohlizec.navigate().to(url);
        klikniNaBtnPrihlasit();
        prihlaseniRodice();

        WebElement jmenoUzivatele = prohlizec.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/div[2]/div/a"));
        String textJmeno = jmenoUzivatele.getText();
        Assertions.assertEquals(uzivatel, textJmeno);
    }

    @Test
    public void rodicJeSchopnyVybratKurz() {
        prohlizec.navigate().to(url);
        String nahodnyZak = "Otík" + (int) (Math.random() * 100);
        vyberKurzu();
        prihlaseniRodice();
        prihlaseniZaka(nahodnyZak);
        overeniPrihlaseniZaka(nahodnyZak);
    }

    @Test
    public void rodicJeSchopnySePrihlasitAVybratKurz() {
        prohlizec.navigate().to(url);
        String nahodnyZak = "Otík" + (int) (Math.random() * 100);
        WebElement btnPrihlasit = prohlizec.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/div[2]/a"));
        btnPrihlasit.click();

        prihlaseniRodice();

        WebElement btnDomu = prohlizec.findElement(By.xpath("//a[contains(text(),'Domů')]"));
        btnDomu.click();

        vyberKurzu();
        prihlaseniZaka(nahodnyZak);
        overeniPrihlaseniZaka(nahodnyZak);
    }

    @Test
    public void rodicJeSchopnySeOdhlasit() {
        prohlizec.navigate().to(url);
        klikniNaBtnPrihlasit();
        prihlaseniRodice();

        WebElement jmenoUzivatele = prohlizec.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/div[2]/div/a"));
        jmenoUzivatele.click();
        WebElement btnOdhlasit = prohlizec.findElement(By.xpath("//a[contains(text(),'Odhlásit')]"));
        btnOdhlasit.click();
        WebElement btnPrihlasit = prohlizec.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/div[2]/a"));
        String btnPrihlasitText = btnPrihlasit.getText();
        Assertions.assertEquals(btnPrihlasitText, "Přihlásit");
    }

    private void klikniNaBtnPrihlasit() {
        WebElement btnPrihlasit = prohlizec.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/div[2]/a"));
        btnPrihlasit.click();
    }

    private void prihlaseniRodice() {
        WebElement poleEmail = prohlizec.findElement(By.id("email"));
        poleEmail.sendKeys(uzivatelEmail);
        WebElement poleHeslo = prohlizec.findElement(By.id("password"));
        poleHeslo.sendKeys(uzivatelHeslo);
        WebElement btnPotvzeniPrihlaseni = prohlizec.findElement(By.xpath("//button[contains(text(),'Přihlásit')]"));
        btnPotvzeniPrihlaseni.click();
    }

    private void prihlaseniZaka(String zak) {
        WebElement termin = prohlizec.findElement(By.className("filter-option-inner-inner"));
        termin.click();
        WebElement vyberTerminu = prohlizec.findElement(By.xpath("//div[@class = 'bs-searchbox']//input"));
        vyberTerminu.sendKeys("11.12.");
        vyberTerminu.sendKeys("\n");
        WebElement jmenoZaka = prohlizec.findElement(By.id("forename"));
        jmenoZaka.sendKeys(zak);
        WebElement prijmeniZaka = prohlizec.findElement(By.id("surname"));
        prijmeniZaka.sendKeys("Carda");
        WebElement datumNarozeniZaka = prohlizec.findElement(By.id("birthday"));
        datumNarozeniZaka.sendKeys("01.01.2010");
        WebElement formaUhrady = prohlizec.findElement(By.xpath("//label[contains(text(),'Hotově')]"));
        formaUhrady.click();
        WebElement souhlasSPodminkami = prohlizec.findElement(By.xpath("//label[contains(text(),'Souhlasím s všeobecnými podmínkami')]"));
        souhlasSPodminkami.click();
        WebElement vytvoritPrihlaskuProZaka = prohlizec.findElement(By.xpath("//input[@value= 'Vytvořit přihlášku']"));
        vytvoritPrihlaskuProZaka.click();
    }

    private void vyberKurzu() {
        WebElement vyberKurzuDaTestovani = prohlizec.findElement(By.xpath("//a[contains(text(),'Více informací')]"));
        vyberKurzuDaTestovani.click();
        WebElement vytvoritPrihlasku = prohlizec.findElement(By.xpath("//div[2]/div/div/div[2]/a"));
        vytvoritPrihlasku.click();
    }

    private void overeniPrihlaseniZaka(String zak) {
        WebElement seznamPrihlasek = prohlizec.findElement(By.xpath("//a[contains(text(),'Přihlášky')]"));
        seznamPrihlasek.click();
        WebElement vyhledaniZaka = prohlizec.findElement(By.xpath("//*[@id=\"DataTables_Table_0_filter\"]/label/input"));
        vyhledaniZaka.sendKeys(zak);
        WebElement jmenoZakaVPrihlasce = prohlizec.findElement(By.xpath("//td[contains(text(),'" + zak + " Carda')]"));
        String textJmeno = jmenoZakaVPrihlasce.getText();
        Assertions.assertEquals(zak + " Carda", textJmeno);
    }

    @AfterEach
    public void tearDown() {
        prohlizec.quit();
    }
}
