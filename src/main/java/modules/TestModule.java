package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.CatalogPage;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CatalogPage.class);
    }

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }
}