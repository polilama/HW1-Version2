package pages;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Course {
    private final static String OTUS_URL = "https://otus.ru";
    private String title;
    private Date startDate;
    private String url;
    private WebDriver driver;

    @Inject
    public Course( WebDriver driver) {
        this.driver = driver;
    }

    @Inject
    public Course(String title, Date startDate) {
        this.title = title;
        this.startDate = startDate;
        this.url = "/course/";
    }

    @Inject
    public Course(String title, Date startDate, WebDriver driver) {
        this.title = title;
        this.startDate = startDate;
        this.url = "/course/";
        this.driver = driver;
    }



    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Date getStartDate() {
        return startDate;
    }

    public static List<Course> fromWebElements(List<WebElement> courseCards) {
        return courseCards.stream()
                .map(card -> {
                    String title = card.findElement(By.cssSelector(".course-card__title")).getText();
                    String dateString = card.findElement(By.cssSelector(".course-card__start-date")).getText();
                    Date startDate = parseDate(dateString);
                    return new Course(title, startDate);
                })
                .toList();
    }

    public static Optional<Course> findEarliestCourse(List<Course> courses) {
        return courses.stream()
                .reduce((course1, course2) -> course1.getStartDate().before(course2.getStartDate()) ? course1 : course2);
    }

    public static Optional<Course> findLatestCourse(List<Course> courses) {
        return courses.stream()
                .reduce((course1, course2) -> course1.getStartDate().after(course2.getStartDate()) ? course1 : course2);
    }

    private static Date parseDate(String dateString) {
        try {
            var dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("ru"));
            return dateFormat.parse(dateString.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static void verifyCourseDetails(WebDriver driver, Course course) {
        String courseUrl = OTUS_URL + course.getUrl();
        driver.get(courseUrl);

        WebElement coursePageTitle = driver.findElement(By.cssSelector(".course-page__title"));
        WebElement coursePageStartDate = driver.findElement(By.cssSelector(".course-page__start-date"));

        String coursePageStartDateString = coursePageStartDate.getText().trim();
        assertEquals(course.getTitle(), coursePageTitle.getText().trim());
        assertTrue(coursePageStartDateString.contains(course.getStartDate().toString()),
                "Дата на странице курса не совпадает с датой курса: " + coursePageStartDateString);
    }

    public void openRandomCategory() {
        driver.get(OTUS_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__next']//button[contains(@class, 'menu-button')]")));

        Actions actions = new Actions(driver);
        actions.moveToElement(menu).perform();

        List<WebElement> categories = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".sc-1pgqitk-0.dNitgt")));
        if (categories.isEmpty()) {
            throw new RuntimeException("Категории не найдены!");
        }

        Random random = new Random();
        WebElement randomCategory = categories.get(random.nextInt(categories.size()));

        String categoryName = randomCategory.getText();
        String categoryUrl = randomCategory.getAttribute("href");

        randomCategory.click();

        wait.until(ExpectedConditions.urlContains(categoryUrl));

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains(categoryUrl),
                "URL не соответствует выбранной категории. Ожидаемый: " + categoryUrl + ", полученный: " + currentUrl);

        System.out.println("Открыта категория: " + categoryName);
    }
}