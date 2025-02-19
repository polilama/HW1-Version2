import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.TestModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.CatalogPage;
import pages.Course;
import pages.CourseUrlProvider;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CatalogTest {
    private final static String COURSE_NAME = "Нагрузочное тестирование";

    @Inject
    private WebDriver driver;
    @Inject
    private CatalogPage catalogPage;

    @BeforeAll
    public void setUp() {
        Injector injector = Guice.createInjector(new TestModule());
        ((Injector) injector).injectMembers(this);
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testOpenCourse() {
        String courseUrl = CourseUrlProvider.getCourseUrl();
        catalogPage.open(courseUrl);
        var findCourse = catalogPage.findCourseByName(COURSE_NAME);

        if (findCourse.isPresent()) {
            catalogPage.clickCourse(findCourse.get());
            var actualCourseName = "<h1 class=\"sc-1x9oq14-0 sc-s2pydo-1 kswXpy diGrSa\">Нагрузочное тестирование</h1>";
            assertEquals(COURSE_NAME, actualCourseName, "Открыта верная страница курса!");
        } else {
            throw new RuntimeException("Курс с именем " + COURSE_NAME + " не найден!");
        }
    }

    @Test
    public void findEarliestAndLatestCourses() {
        String courseUrl = CourseUrlProvider.getCourseUrl();
        catalogPage.open(courseUrl);
        var courseCards = driver.findElements(By.cssSelector(".course-card"));
        var courses = Course.fromWebElements(courseCards);
        var earliestCourse = Course.findEarliestCourse(courses);
        var latestCourse = Course.findLatestCourse(courses);

        earliestCourse.ifPresent(course -> Course.verifyCourseDetails(driver, course));
        latestCourse.ifPresent(course -> Course.verifyCourseDetails(driver, course));
    }

    @Test
    public void openRandomCategory() {
        var course = new Course(driver);
        course.openRandomCategory();
    }
}