package com.neudesic.qa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
//        try (Playwright playwright = Playwright.create()) {
////            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
//            Browser browser = Playwright.create().chromium().connect("ws://127.0.0.1:3000/");
//            BrowserContext context = browser.newContext();
//            Page page = context.newPage();
//            page.navigate("https://playwright.dev");
//            assertThat(page).hasTitle(Pattern.compile("Playwright"));
//            Locator getStarted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get Started"));
//            assertThat(getStarted).hasAttribute("href", "/docs/intro");
//            getStarted.click();
//            assertThat(page.getByRole(AriaRole.HEADING,
//                    new Page.GetByRoleOptions().setName("Installation"))).isVisible();
//            System.out.println(page.title());
//            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example1.png")));
//            context.close();
//            context = browser.newContext();
//            page = context.newPage();
//            page.navigate("https://demo.playwright.dev/todomvc/#/");
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("My first todo");
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Another todo i added");
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
//            page.getByText("My first todo").click();
//            page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("My first todo")).getByLabel("Toggle Todo").check();
//            page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText("Another todo i added")).getByLabel("Toggle Todo").check();
//            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
//            page.getByTestId("todo-title").click();
//            page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Toggle Todo")).uncheck();
//            page.getByTestId("todo-title").dblclick();
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Edit")).fill("Another todo i added and now edited");
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Edit")).press("Enter");
//            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if this todo is added");
//            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
//            assertThat(page.getByTestId("todo-title")).containsText("Check if this todo is added");
//            assertThat(page.getByTestId("todo-title")).matchesAriaSnapshot("- text: Check if this todo is added");
//            System.out.println(page.title());
//            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example2.png")));
//            context.close();
//            browser.close();
//        }
    }
}
