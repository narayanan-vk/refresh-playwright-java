package com.neudesic.qa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.neudesic.qa.configs.BaseTest;
import com.neudesic.qa.configs.ExecutionOptions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ExecutionOptions.CustomOptions.class)
public class AppTest extends BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(AppTest.class);

    @Epic("Todo MVC UI")
    @Feature("TODO POC")
    @Story("Add TODO")
    @Tag("UI")
    @Tag("P1")
    @Tag("Smoke")
    @Tag("Regression")
    @DisplayName("Should add TODO items")
    @Test
    void shouldCheckAddTodo() {
        getPage().navigate("/todomvc/#/");
        LOGGER.info("Navigated to TODO MVC page.");
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        LOGGER.info("Add a new todo.");
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if this todo is added");
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(getPage().getByTestId("todo-title")).containsText("Check if this todo is added");
        assertThat(getPage().getByTestId("todo-title")).matchesAriaSnapshot("- text: Check if this todo is added");
        LOGGER.info("TODO added successfully.");
    }

    @Epic("Todo MVC UI")
    @Feature("TODO POC")
    @Story("Delete TODO")
    @Tag("UI")
    @Tag("P1")
    @Tag("Smoke")
    @Tag("Regression")
    @DisplayName("Should delete TODO items")
    @Test
    void shouldCheckDeleteTodo() {
        getPage().navigate("/todomvc/#/");
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Check if todo is created then deleted");
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(getPage().getByTestId("todo-title")).containsText("Check if todo is created then deleted");
        getPage().getByTestId("todo-title").getByText("Check if todo is created then deleted").hover();
        getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
        assertThat(getPage().getByTestId("todo-title")).isHidden();
        assertThat(getPage().getByText("This is just a demo of TodoMVC for testing, not the real TodoMVC app. todos")).isVisible();
    }

//    @Test
//    void shouldCheckFruitIsDisplayed(Page getPage()) {
//        getPage().route("*/**/api/v1/fruits", route -> {
//            APIResponse response = route.fetch();
//            byte[] json = response.body();
//            JsonObject parsed = new Gson().fromJson(new String(json), JsonObject.class);
//            parsed.add(new JsonObject().addProperty("name", "Loquat"),);
//
//            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody(parsed.toString()));
//        });
//        getPage().navigate("/api-mocking");
//        assertThat(getPage().getByText("Loquat", new Page.GetByTextOptions().setExact(true))).isVisible();
//    }
}
