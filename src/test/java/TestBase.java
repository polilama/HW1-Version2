import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.TestModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import pages.CatalogPage;

import javax.inject.Inject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {
    @Inject
    protected WebDriver driver;
    @Inject
    protected CatalogPage catalogPage;
    @Inject
    protected Injector injector;

    @BeforeAll
    public void setUp() {
        injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

