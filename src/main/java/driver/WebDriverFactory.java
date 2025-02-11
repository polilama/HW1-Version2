package driver;

import exceptions.BrowserNotSupportedException;
import org.openqa.selenium.WebDriver;

public interface WebDriverFactory {
    WebDriver getDriver() throws BrowserNotSupportedException;
}
