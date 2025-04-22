package com.neudesic.qa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppTest {
    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://demo.playwright.dev/todomvc/#/");
    }

    @AfterEach
    void closeContext() {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "test-output/trace" + timestamp + ".zip";
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get(fileName)));
        context.close();
    }

    @Test
    void shouldCheckAddTodo() {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if this todo is added");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(page.getByTestId("todo-title")).containsText("Check if this todo is added");
        assertThat(page.getByTestId("todo-title")).matchesAriaSnapshot("- text: Check if this todo is added");
    }

    @Test
    void shouldCheckDeleteTodo() {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if todo is created then deleted");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(page.getByTestId("todo-title")).containsText("Check if todo is created then deleted");
        page.getByTestId("todo-title").getByText("Check if todo is created then deleted").hover();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
        assertThat(page.getByTestId("todo-title")).isHidden();
        assertThat(page.getByText("This is just a demo of TodoMVC for testing, not the real TodoMVC app. todos")).isVisible();
    }
}
