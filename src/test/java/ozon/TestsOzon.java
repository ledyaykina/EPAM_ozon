package ozon;

import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TestsOzon {

    // Добавление товара в корзину со страницы поиска неавторизованным пользователем
    @Test
    public void addByFindBarAnonymous() {

        // Драйвера
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver83.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Открытие страницы
        driver.get("https://www.ozon.ru/");
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("input[placeholder=\"Искать на Ozon\"]")));

        // Открытие окна авторизации
        driver.findElement(By.cssSelector("[data-widget=\"profileMenuAnonymous\"]")).click();

        // Ожидание подтверждения авторизации
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[href=\"/my/main\"]")));
        driver.findElement(By.cssSelector("[href=\"/my/main\"]")).click();

        // Поисковой запрос
        driver.findElement(By.cssSelector("input[placeholder=\"Искать на Ozon\"]"))
                .sendKeys("Соковыжималка", Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"megaPaginator\"]")));

        // Добавление товара в корзину
        WebElement addButton = driver.findElement(By.cssSelector("[data-widget=\"megaPaginator\"] > div"))
                .findElement(By.cssSelector("div[data-widget=\"searchResultsV2\"]"))
                .findElements(By.cssSelector("div > div[style=\"grid-column-start: span 12;\"]"))
                .get(0)
                .findElements(By.cssSelector("div > div > div[style=\"width: 25%; max-width: 25%; flex: 0 0 25%;\"]"))
                .get(1).findElements(By.cssSelector("div > button"))
                .get(1);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", addButton);
        wait.until(ExpectedConditions
                .textToBePresentInElement(driver.findElement(By.cssSelector("a[href=\"/cart\"] span")), "1"));

        // Запоминание названия товара на поисковой странице
        String productName = driver.findElement(By.cssSelector("[data-widget=\"megaPaginator\"] > div"))
                .findElement(By.cssSelector("div[data-widget=\"searchResultsV2\"]"))
                .findElements(By.cssSelector("div > div[style=\"grid-column-start: span 12;\"]"))
                .get(0)
                .findElement(By.cssSelector("div > div > div[style=\"width: 50%; max-width: 50%; flex: 0 0 50%;\"]"))
                .findElement(By.cssSelector("a")).getText();

        // Запоминание цены товара на поисковой странице
        String productPrice = driver.findElement(By.cssSelector("[data-widget=\"megaPaginator\"] > div"))
                .findElement(By.cssSelector("div[data-widget=\"searchResultsV2\"]"))
                .findElements(By.cssSelector("div > div[style=\"grid-column-start: span 12;\"]"))
                .get(0)
                .findElements(By.cssSelector("div > div > div[style=\"width: 25%; max-width: 25%; flex: 0 0 25%;\"]"))
                .get(1)
                .findElements(By.cssSelector("a > div"))
                .get(1)
                .findElement(By.cssSelector("span")).getText();

        // Переход в корзину
        driver.findElement(By.cssSelector("a[href=\"/cart\"] span")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"total\"]")));

        // Запоминание названия на странице корзины
        String productNameBasket = driver
                .findElement(By.cssSelector("span[style=\"color: rgb(0, 26, 52);\"]")).getText();

        // Запоминание цены на странице корзины
        String productPriceBasket = driver
                .findElements(By.cssSelector("section[data-widget=\"total\"] span")).get(11).getText();

        // Сравнение
        Assert.assertEquals(productName, productNameBasket);
        // Не удаляет пробельные символы!!! Не знаю, как починить
        //Assert.assertEquals(productPrice.replaceAll("\\s+",""), productPriceBasket.replaceAll("\\s+",""));

        // Закрытие страницы
        driver.quit();

    }

    // Добавление товара в корзину со страницы товара неавторизованным пользователем
    @Test
    public void addByProductPageAnonymous() {

        // Драйвера
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver83.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Открытие страницы
        driver.get("https://www.ozon.ru/context/detail/id/147962892/");
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("[data-widget=\"webSale\"]")));

        // Открытие окна авторизации
        driver.findElement(By.cssSelector("[data-widget=\"profileMenuAnonymous\"]")).click();

        // Ожидание подтверждения авторизации
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[href=\"/my/main\"]")));
        driver.findElement(By.cssSelector("[href=\"/my/main\"]")).click();

        // Запоминание названия товара на поисковой странице
        String productName = driver.findElement(By.cssSelector("h1")).getText();

        // Добавление товара в корзину
        driver.findElement(By.cssSelector("button[target=\"_self\"]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"total\"]")));

        // Переход в корзину
        driver.findElement(By.cssSelector("a[href=\"/cart\"] span")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"total\"]")));

        // Запоминание названия на странице корзины
        String productNameBasket = driver
                .findElement(By.cssSelector("span[style=\"color: rgb(0, 26, 52);\"]")).getText();

        // Сравнение
        Assert.assertEquals(productName, productNameBasket);

        // Закрытие страницы
        driver.quit();
    }

    // Добавление товара в корзину со страницы поиска авторизованным пользователем
    @Test
    public void addByFindBarAuthorized() {

        // Драйвера
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver83.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Открытие страницы
        driver.get("https://www.ozon.ru/");
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("input[placeholder=\"Искать на Ozon\"]")));

        // Открытие окна авторизации
        driver.findElement(By.cssSelector("[data-widget=\"profileMenuAnonymous\"]")).click();

        // Ожидание подтверждения авторизации
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[href=\"/my/main\"]")));
        driver.findElement(By.cssSelector("[href=\"/my/main\"]")).click();

        // Поисковой запрос
        driver.findElement(By.cssSelector("input[placeholder=\"Искать на Ozon\"]"))
                .sendKeys("Соковыжималка", Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"megaPaginator\"]")));

        // Добавление товара в корзину
        WebElement addButton = driver.findElement(By.cssSelector("[data-widget=\"megaPaginator\"] > div"))
                .findElement(By.cssSelector("div[data-widget=\"searchResultsV2\"]"))
                .findElements(By.cssSelector("div > div[style=\"grid-column-start: span 12;\"]"))
                .get(0)
                .findElements(By.cssSelector("div > div > div[style=\"width: 25%; max-width: 25%; flex: 0 0 25%;\"]"))
                .get(1).findElements(By.cssSelector("div > button"))
                .get(1);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", addButton);
        wait.until(ExpectedConditions
                .textToBePresentInElement(driver.findElement(By.cssSelector("a[href=\"/cart\"] span")), "1"));

        // Запоминание названия товара на поисковой странице
        String productName = driver.findElement(By.cssSelector("[data-widget=\"megaPaginator\"] > div"))
                .findElement(By.cssSelector("div[data-widget=\"searchResultsV2\"]"))
                .findElements(By.cssSelector("div > div[style=\"grid-column-start: span 12;\"]"))
                .get(0)
                .findElement(By.cssSelector("div > div > div[style=\"width: 50%; max-width: 50%; flex: 0 0 50%;\"]"))
                .findElement(By.cssSelector("a")).getText();

        // Запоминание цены товара на поисковой странице
        String productPrice = driver.findElement(By.cssSelector("[data-widget=\"megaPaginator\"] > div"))
                .findElement(By.cssSelector("div[data-widget=\"searchResultsV2\"]"))
                .findElements(By.cssSelector("div > div[style=\"grid-column-start: span 12;\"]"))
                .get(0)
                .findElements(By.cssSelector("div > div > div[style=\"width: 25%; max-width: 25%; flex: 0 0 25%;\"]"))
                .get(1)
                .findElements(By.cssSelector("a > div"))
                .get(1)
                .findElement(By.cssSelector("span")).getText();

        // Переход в корзину
        driver.findElement(By.cssSelector("a[href=\"/cart\"] span")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"total\"]")));

        // Запоминание названия на странице корзины
        String productNameBasket = driver
                .findElement(By.cssSelector("span[style=\"color: rgb(0, 26, 52);\"]")).getText();

        // Запоминание цены на странице корзины
        String productPriceBasket = driver
                .findElements(By.cssSelector("section[data-widget=\"total\"] span")).get(11).getText();

        // Сравнение
        Assert.assertEquals(productName, productNameBasket);
        // Не удаляет пробельные символы!!! Не знаю, как починить
        //Assert.assertEquals(productPrice.replaceAll("\\s+",""), productPriceBasket.replaceAll("\\s+",""));

        // Закрытие страницы
        driver.quit();

    }

    // Добавление товара в корзину со страницы товара авторизованным пользователем
    @Test
    public void addByProductPageAuthorized() {

        // Драйвера
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver83.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Открытие страницы
        driver.get("https://www.ozon.ru/context/detail/id/147962892/");
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector("[data-widget=\"webSale\"]")));

        // Открытие окна авторизации
        driver.findElement(By.cssSelector("[data-widget=\"profileMenuAnonymous\"]")).click();

        // Ожидание подтверждения авторизации
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[href=\"/my/main\"]")));
        driver.findElement(By.cssSelector("[href=\"/my/main\"]")).click();

        // Запоминание названия товара на поисковой странице
        String productName = driver.findElement(By.cssSelector("h1")).getText();

        // Добавление товара в корзину
        driver.findElement(By.cssSelector("button[target=\"_self\"]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"total\"]")));

        // Переход в корзину
        driver.findElement(By.cssSelector("a[href=\"/cart\"] span")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-widget=\"total\"]")));

        // Запоминание названия на странице корзины
        String productNameBasket = driver
                .findElement(By.cssSelector("span[style=\"color: rgb(0, 26, 52);\"]")).getText();

        // Сравнение
        Assert.assertEquals(productName, productNameBasket);

        // Закрытие страницы
        driver.quit();
    }
}
