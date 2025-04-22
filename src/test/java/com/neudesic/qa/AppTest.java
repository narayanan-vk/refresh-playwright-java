package com.neudesic.qa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(AppTest.CustomOptions.class)
public class AppTest {

    public static class CustomOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setContextOptions(new Browser.NewContextOptions()
                            .setBaseURL("https://demo.playwright.dev")).setTrace(Options.Trace.ON);
        }
    }

    @Test
    void shouldCheckAddTodo(Page page) {
//        page.navigate("https://demo.playwright.dev/todomvc/#/");
        page.navigate("/todomvc/#/");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if this todo is added");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(page.getByTestId("todo-title")).containsText("Check if this todo is added");
        assertThat(page.getByTestId("todo-title")).matchesAriaSnapshot("- text: Check if this todo is added");
    }

    @Test
    void shouldCheckDeleteTodo(Page page) {
//        page.navigate("https://demo.playwright.dev/todomvc/#/");
        page.navigate("/todomvc/#/");
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
