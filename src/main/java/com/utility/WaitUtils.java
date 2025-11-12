package com.utility;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseclass.DriverManager;

public class WaitUtils {
	
	 private static final int DEFAULT_TIMEOUT = 20;
	    private static final int DEFAULT_POLLING = 500;

	public static void waitUntilVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(DriverManager.webDriver.get(), Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitUntilClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(DriverManager.webDriver.get(), Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static void safeClickJS(WebElement element) {
		try {
			waitUntilVisible(element);
			waitUntilClickable(element);
			scrollToElement(element);
			element.click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
			js.executeScript("arguments[0].click();", element);
		}
	}

	public static void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void safeClicked(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(DriverManager.webDriver.get(), Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			scrollToElement(element);
			element.click();
		} catch (Exception e) {
			System.out.println("Standard click failed, trying JS click: " + e.getMessage());
			try {
				WebElement element = DriverManager.webDriver.get().findElement(locator);
				JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
				js.executeScript("arguments[0].scrollIntoView(true);", element);
				js.executeScript("arguments[0].click();", element);
			} catch (Exception inner) {
				throw new RuntimeException("JS click also failed: " + inner.getMessage());
			}
		}
	}

	public static void safeClick(WebElement element) {
		try {
			waitUntilVisible(element);
			waitUntilClickable(element);
			element.click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
			js.executeScript("arguments[0].click();", element);
		}
	}

	public static void safeSendKeys(WebElement element, String value) {
		try {
			waitUntilVisible(element);
			waitUntilClickable(element);
			element.clear(); // optional, clears existing text
			element.sendKeys(value);
		} catch (Exception e) {
			// fallback using JS if needed
			JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
			js.executeScript("arguments[0].value='" + value + "';", element);
		}
	}

	// Actions Class

	public static void mouseHover(WebElement element) {
		try {
			// Wait for the element to be visible first
			waitUntilVisible(element);

			// Scroll it into view to make sure it's in the viewport
			scrollToElement(element);

			// Perform mouse hover action
			Actions actions = new Actions(DriverManager.webDriver.get());
			actions.moveToElement(element).pause(Duration.ofMillis(300)).perform();

			System.out.println("Mouse hover successful on element: " + element);

		} catch (Exception e) {
			System.out.println("Standard hover failed: " + e.getMessage());
			try {
				// Fallback to JavaScript hover (simulates mouseenter event)
				JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
				js.executeScript(
						"var event = new MouseEvent('mouseover', {bubbles: true}); arguments[0].dispatchEvent(event);",
						element);
				System.out.println("Fallback JS hover executed.");
			} catch (Exception inner) {
				throw new RuntimeException("Both standard and JS hover failed: " + inner.getMessage());
			}
		}
	}

	public static WebElement waitUntilVisible(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverManager.webDriver.get(), Duration.ofSeconds(20));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public static void waitUntilVisibleOr(By locator1, By locator2) throws TimeoutException {
		WebDriver driver = DriverManager.webDriver.get();
		long endTime = System.currentTimeMillis() + 20000; // 20 seconds timeout

		while (System.currentTimeMillis() < endTime) {
			try {
				if (driver.findElement(locator1).isDisplayed() || driver.findElement(locator2).isDisplayed()) {
					return;
				}
			} catch (Exception ignored) {
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		throw new TimeoutException("Neither locator became visible within timeout.");
	}

	public static void jsClick(WebDriver driver, WebElement element) {
		try {
			scrollToElement(element);
			JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
			js.executeScript("arguments[0].click();", element);
			System.out.println("✅ JS click performed on element: " + element);
		} catch (Exception e) {
			System.out.println("❌ JS click failed: " + e.getMessage());
			throw new RuntimeException("JS click failed on element: " + element, e);
		}
	}

	public static void jsClick(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
			js.executeScript("arguments[0].click();", element);
			System.out.println("Clicked element via JS: " + element);
		} catch (Exception e) {
			System.out.println("❌ JS Click failed for element: " + e.getMessage());
		}
	}

	public static void safeClickBYJs(WebElement element) {
		for (int attempt = 0; attempt < 3; attempt++) {
			try {
				waitUntilVisible(element);
				waitUntilClickable(element);
				element.click();
				return;
			} catch (org.openqa.selenium.StaleElementReferenceException e) {
				System.out.println("⚠️ Stale element on click attempt " + (attempt + 1) + " — retrying...");
				try {
					Thread.sleep(500);
				} catch (InterruptedException ignored) {
				}
			} catch (Exception e) {
				System.out.println(" Click failed: " + e.getMessage());
				JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
				js.executeScript("arguments[0].click();", element);
				return;
			}
		}
		throw new RuntimeException("❌ Element stayed stale after retries: " + element);
	}

	
	public static WebElement fluentWaitForElement(WebElement element, int timeoutSeconds, int pollingMillis) {
		org.openqa.selenium.support.ui.FluentWait<WebDriver> wait = new org.openqa.selenium.support.ui.FluentWait<>(
				DriverManager.webDriver.get()).withTimeout(Duration.ofSeconds(timeoutSeconds))
				.pollingEvery(Duration.ofMillis(pollingMillis))
				.ignoring(org.openqa.selenium.NoSuchElementException.class)
				.ignoring(org.openqa.selenium.StaleElementReferenceException.class)
				.ignoring(org.openqa.selenium.ElementClickInterceptedException.class)
				.withMessage("❌ Element not found within timeout using FluentWait.");

		// Use old-school way, not streams
		return wait.until(driver -> {
			try {
				if (element != null && element.isDisplayed() && element.isEnabled()) {
					System.out.println("✅ Element found and ready: " + element);
					return element;
				} else {
					return null;
				}
			} catch (Exception e) {
				return null;
			}
		});
	}
	
	// ===============================
	// 🟢 Click with Fluent Wait
	// ===============================
	public static void clickWithFluentWait(WebElement element) {
        try {
            // Wait for the element to be visible and stable
            WebElement readyElement = fluentWaitForElement(element, DEFAULT_TIMEOUT, DEFAULT_POLLING);

            // Scroll into view for safety
            scrollToElement(readyElement);

            // Click safely
            safeClick(readyElement);
            System.out.println("✅ Clicked element using FluentWait: " + readyElement);

        } catch (Exception e) {
            System.out.println("⚠️ FluentWait click failed, attempting JS fallback: " + e.getMessage());
            try {
                JavascriptExecutor js = (JavascriptExecutor) DriverManager.webDriver.get();
                js.executeScript("arguments[0].click();", element);
                System.out.println("✅ JS click fallback successful for: " + element);
            } catch (Exception inner) {
                throw new RuntimeException("❌ FluentWait + JS click both failed for: " + element, inner);
            }
        }
    }
	
	

	
	

}
