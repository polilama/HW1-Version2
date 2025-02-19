package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class CatalogPage {
    private WebDriver driver;

    @FindBy(css = ".course-card")
    private List<WebElement> courseCards;

    @Inject
    public CatalogPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        driver.get(url);
    }

    public Optional<WebElement> findCourseByName(String courseName) {
        return courseCards.stream()
                .filter(element -> element.getText().equalsIgnoreCase(courseName))
                .findFirst();
    }

    public void clickCourse(WebElement course) {
        highlightElement(course);
        course.click();
    }

    private void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }
}


