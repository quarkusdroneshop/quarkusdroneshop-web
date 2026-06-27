package io.quarkusdroneshop.web;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.*;

public class WebTestQDCA105Pro3E2ETest {
  public static void main(String[] args) {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        .setHeadless(true));
      BrowserContext context = browser.newContext();
      Page page = context.newPage();
      page.navigate("http://localhost:8080/");
      page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Place an Order")).click();
      // QDC-A105-Pro03 is the 8th Add button (nth 7)
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).nth(7).click();
      // Wait for modal to be visible before interacting
      page.locator("#myModal").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
      page.locator("#item_form #name").fill("nmushino@redhat.com");
      // Click the Add submit button inside item_form to add item to order
      page.locator("#item_form button[type='submit']").click();
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Place Order")).click();
      page.close();
    }
  }
}