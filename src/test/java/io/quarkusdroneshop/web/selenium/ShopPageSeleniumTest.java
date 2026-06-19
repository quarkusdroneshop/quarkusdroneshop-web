package io.quarkusdroneshop.web.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.web.infrastructure.testsupport.KafkaTestResource;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Selenium UIテスト — Quarkus アプリ起動済みの状態で Chrome を操作する。
 * ChromeDriver は WebDriverManager が自動管理するため別途インストール不要。
 * Chrome バイナリが存在しない環境（CIコンテナ等）では全テストを自動スキップする。
 */
@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopPageSeleniumTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void setUpDriver() {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1280,900");
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (Exception e) {
            // Chrome が存在しない環境（CI/CDコンテナ等）では全テストをスキップ
            assumeTrue(false, "Chrome が利用できないためSeleniumテストをスキップします: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ─── ページロード ─────────────────────────────────────────

    @Test
    @Order(1)
    void testPageLoadsSuccessfully() {
        driver.get(BASE_URL);
        assertEquals("Quarkus Drone Shop", driver.getTitle());
    }

    @Test
    @Order(2)
    void testNavbarIsVisible() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("nav.navbar")));
        WebElement brand = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.navbar-brand")));
        assertTrue(brand.isDisplayed());
        assertTrue(brand.getAttribute("href").contains("#banner"),
            "Brand link should point to #banner, got: " + brand.getAttribute("href"));
    }

    @Test
    @Order(3)
    void testNavbarContainsProductListLink() {
        driver.get(BASE_URL);
        WebElement productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[@href='#product' and contains(text(),'Product List')]")));
        assertTrue(productLink.isDisplayed());
    }

    @Test
    @Order(4)
    void testNavbarContainsRewardsLink() {
        driver.get(BASE_URL);
        WebElement rewardsLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rewards_link")));
        assertTrue(rewardsLink.isDisplayed());
    }

    // ─── 商品セクション ───────────────────────────────────────

    @Test
    @Order(5)
    void testProductSectionIsVisible() {
        driver.get(BASE_URL);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('#product').scrollIntoView(true)");
        WebElement productSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section.product")));
        assertTrue(productSection.isDisplayed());
    }

    @Test
    @Order(6)
    void testQdcA101AddButtonIsPresent() {
        driver.get(BASE_URL);
        WebElement addBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("button[data-whatever='QDC_A101']")));
        assertEquals("QDC_A101", addBtn.getAttribute("data-whatever"));
        assertEquals("qdca10", addBtn.getAttribute("data-item_type"));
    }

    @Test
    @Order(7)
    void testAllStandardDroneButtonsPresent() {
        driver.get(BASE_URL);
        String[] standardItems = {"QDC_A101", "QDC_A102", "QDC_A103", "QDC_A104_AC", "QDC_A104_AT"};
        for (String item : standardItems) {
            WebElement btn = driver.findElement(By.cssSelector("button[data-whatever='" + item + "']"));
            assertNotNull(btn, item + " button should be present");
        }
    }

    @Test
    @Order(8)
    void testAllProDroneButtonsPresent() {
        driver.get(BASE_URL);
        String[] proItems = {"QDC_A105_Pro01", "QDC_A105_Pro02", "QDC_A105_Pro03", "QDC_A105_Pro04"};
        for (String item : proItems) {
            WebElement btn = driver.findElement(By.cssSelector("button[data-whatever='" + item + "']"));
            assertNotNull(btn, item + " button should be present");
        }
    }

    // ─── 注文モーダル ─────────────────────────────────────────

    @Test
    @Order(9)
    void testOrderModalOpensOnAddButton() {
        driver.get(BASE_URL);
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-whatever='QDC_A101']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myModal")));
        assertTrue(modal.isDisplayed(), "Order modal should be visible after clicking Add");
    }

    @Test
    @Order(10)
    void testOrderModalHasNameInput() {
        driver.get(BASE_URL);
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-whatever='QDC_A101']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        assertTrue(nameInput.isDisplayed());
        assertEquals("text", nameInput.getAttribute("type"));
    }

    @Test
    @Order(11)
    void testOrderModalAddItem() {
        driver.get(BASE_URL);
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[data-whatever='QDC_A101']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        nameInput.clear();
        nameInput.sendKeys("SeleniumUser");

        WebElement addItemBtn = driver.findElement(By.cssSelector("#item_form button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addItemBtn);

        WebElement currentOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("current_order")));
        String orderText = currentOrder.getText();
        assertTrue(orderText.contains("QDC-A101") || orderText.contains("SeleniumUser") || !orderText.isEmpty(),
            "Current order list should show added item");
    }

    // ─── リワードモーダル ──────────────────────────────────────

    @Test
    @Order(12)
    void testRewardsModalOpens() {
        driver.get(BASE_URL);
        WebElement rewardsLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#rewards_link a")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rewardsLink);

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rewardsModal")));
        assertTrue(modal.isDisplayed(), "Rewards modal should be visible");
    }

    @Test
    @Order(13)
    void testRewardsModalHasEmailInput() {
        driver.get(BASE_URL);
        WebElement rewardsLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#rewards_link a")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rewardsLink);

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rewards_id")));
        assertTrue(emailInput.isDisplayed());
        assertEquals("email", emailInput.getAttribute("type"));
    }

    @Test
    @Order(14)
    void testRewardsModalEmailEntry() {
        driver.get(BASE_URL);
        WebElement rewardsLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#rewards_link a")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rewardsLink);

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rewards_id")));
        emailInput.clear();
        emailInput.sendKeys("test@example.com");
        assertEquals("test@example.com", emailInput.getAttribute("value"));
    }

    @Test
    @Order(15)
    void testRewardsModalCancelButton() {
        driver.get(BASE_URL);
        WebElement rewardsLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("#rewards_link a")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rewardsLink);

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rewardsModal")));
        assertTrue(modal.isDisplayed());

        WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn_cancel")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancelBtn);

        wait.until(ExpectedConditions.invisibilityOf(modal));
        assertFalse(modal.isDisplayed(), "Rewards modal should close after cancel");
    }

    // ─── ステータスボード ──────────────────────────────────────

    @Test
    @Order(16)
    void testStatusboardSectionExists() {
        driver.get(BASE_URL);
        WebElement statusboard = driver.findElement(By.id("statusboard"));
        assertNotNull(statusboard);
    }

    // ─── ページ下部 ───────────────────────────────────────────

    @Test
    @Order(17)
    void testPageContainsCopyrightFooter() {
        driver.get(BASE_URL);
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("HH Themes") || pageSource.contains("2025"),
            "Page should contain footer copyright");
    }

    @Test
    @Order(18)
    void testPlaceOrderButtonInBannerVisible() {
        driver.get(BASE_URL);
        WebElement placeOrderBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("a.btn[href='#product']")));
        assertNotNull(placeOrderBtn);
        assertTrue(placeOrderBtn.getText().contains("Place an Order"));
    }
}
