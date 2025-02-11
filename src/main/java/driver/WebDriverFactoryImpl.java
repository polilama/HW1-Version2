package driver;

import data.Browser;
import exceptions.BrowserNotSupportedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.opera.OperaDriver;

import javax.inject.Inject;
import java.util.Locale;
import java.util.stream.Stream;

public class WebDriverFactoryImpl implements WebDriverFactory {
    private final String browserName;

    @Inject
    public WebDriverFactoryImpl() {
        browserName = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
    }

    public WebDriver getDriver() {
        var isSupported = Stream.of(Browser.values())
                .anyMatch(browserData -> browserName.equalsIgnoreCase(browserData.name().toLowerCase(Locale.ROOT)));

        if (!isSupported) {
            throw new BrowserNotSupportedException(browserName);
        }

        switch (Browser.valueOf(browserName.toUpperCase(Locale.ROOT))) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            }
            case OPERA -> {
                WebDriverManager.operadriver().setup();
                return new OperaDriver();
            }
            default -> throw new BrowserNotSupportedException(browserName);
        }
    }
}
