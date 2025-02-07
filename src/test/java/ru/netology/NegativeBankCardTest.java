package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeBankCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void EmptyNameField() { //Пустое поле "Фамилия и Имя"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Поле обязательно для заполнения",
                form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void NameFieldInLatin() { //поле "Фамилия и Имя" на латинице
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Petrov Petr");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void NumbersInTheNameField() { //цифры в поле "Фамилия и Имя"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("12345678");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void SpecialCharactersInTheNameField() { //спецсимволы в поле "Фамилия и Имя"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("@#$%^&*");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void LettersAndNumbersInTheNameField() { //цифры и буквы в поле "Фамилия и Имя"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петр0в П3тр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void LettersAndSpecialCharactersInTheNameField() { //спецсимволы и буквы в поле "Фамилия и Имя"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петр@в П&тр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void EmptyPhoneField() { //пустое поле "телефон"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Поле обязательно для заполнения",
                form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void WithoutThePlusSignPhoneField() { //без знака "+" в поле "телефон"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89991234567");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void Enter10DigitsInThePhoneField() { //10 цифр в поле "телефон"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7999123456");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void Enter12DigitsInThePhoneField() { //12 цифр в поле "телефон"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+799912345678");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void LettersInPhoneField() { //буквы в поле "телефон"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("Привет");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void SpecialCharactersInPhoneField() { //спецсимволы в поле "телефон"
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("@#$%$^&@!");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void EmptyCheckbox() { //пустой чекбокс
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Петр");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        form.findElement(By.cssSelector("button")).click();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй",
                form.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText().trim());
    }
}