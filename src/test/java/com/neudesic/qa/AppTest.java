package com.neudesic.qa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.neudesic.qa.configs.ExecutionOptions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ExecutionOptions.CustomOptions.class)
public class AppTest {

    @Epic("Todo MVC UI")
    @Feature("TODO POC")
    @Story("Add TODO")
    @Tag("UI")
    @Tag("P1")
    @Tag("Smoke")
    @Tag("Regression")
    @Test
    void shouldCheckAddTodo(Page page) {
        page.navigate("/todomvc/#/");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if this todo is added");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(page.getByTestId("todo-title")).containsText("Check if this todo is added");
        assertThat(page.getByTestId("todo-title")).matchesAriaSnapshot("- text: Check if this todo is added");
    }

    @Epic("Todo MVC UI")
    @Feature("TODO POC")
    @Story("Delete TODO")
    @Tag("UI")
    @Tag("P1")
    @Tag("Smoke")
    @Tag("Regression")
    @Test
    void shouldCheckDeleteTodo(Page page) {
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

//    @Test
//    void shouldCheckFruitIsDisplayed(Page page) {
//        page.route("*/**/api/v1/fruits", route -> {
//            APIResponse response = route.fetch();
//            byte[] json = response.body();
//            JsonObject parsed = new Gson().fromJson(new String(json), JsonObject.class);
//            parsed.add(new JsonObject().addProperty("name", "Loquat"),);
//
//            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody(parsed.toString()));
//        });
//        page.navigate("/api-mocking");
//        assertThat(page.getByText("Loquat", new Page.GetByTextOptions().setExact(true))).isVisible();
//    }
}
