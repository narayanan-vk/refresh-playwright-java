package com.neudesic.qa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.neudesic.qa.configs.BaseTest;
import com.neudesic.qa.configs.ExecutionOptions;
import io.qameta.allure.*;
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
    @Owner("NVK")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test attempts to add a TODO item and then verify that the added item is saved and visible on the page.")
    @Test
    void shouldCheckAddTodo() {
        getPage().navigate("/todomvc/#/");
        LOGGER.info("Navigated to TODO MVC page.");
        String todoItemName = "Check if this todo is added";
        addTodoItem(todoItemName);
        assertThat(getPage().getByTestId("todo-title")).containsText(todoItemName);
        assertThat(getPage().getByTestId("todo-title")).matchesAriaSnapshot("- text: Check if this todo is added");
        LOGGER.info("TODO added successfully.");
    }

    @Step("Add TODO item")
    void addTodoItem(String todoItemName){
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        LOGGER.info("Add a new todo.");
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill(todoItemName);
        getPage().getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
    }

    @Epic("Todo MVC UI")
    @Feature("TODO POC")
    @Story("Delete TODO")
    @Tag("UI")
    @Tag("P1")
    @Tag("Smoke")
    @Tag("Regression")
    @Severity(SeverityLevel.NORMAL)
    @Owner("NVK")
    @DisplayName("Should delete TODO items")
    @Description("This test attempts to add a TODO item and then delete that TODO item. Afterwards, the TODO item is verified to be hidden in the page.")
    @Test
    void shouldCheckDeleteTodo() {
        getPage().navigate("/todomvc/#/");
        String todoItemName = "Check if todo is created then deleted";
        addTodoItem(todoItemName);
        assertThat(getPage().getByTestId("todo-title")).containsText(todoItemName);
        getPage().getByTestId("todo-title").getByText(todoItemName).hover();
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
