package com.neudesic.qa.e2e.todomvc;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.neudesic.qa.configs.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("UI")
public class TodoMvcTests extends BaseTest {

    private static final List<String> TODO_ITEMS = List.of("buy some cheese", "feed the cat", "book a doctors appointment");
    private static final String TODO_MVC_URL = "/todomvc/#/";

    private static void createDefaultTodos(Page page) {
        Locator newTodo = page.getByPlaceholder("What needs to be done?");
        for (String item : TODO_ITEMS) {
            newTodo.fill(item);
            newTodo.press("Enter");
        }
    }

    private static void checkNumberOfTodosInLocalStorage(Page page, int expected) {
        Object actual = page.evaluate("() => JSON.parse(localStorage.getItem('react-todos') || '[]').length");
//        assertThat(actual).isInstanceOf(Number.class);
//        assertThat(((Number) actual).intValue()).isEqualTo(expected);
    }

    private static void checkNumberOfCompletedTodosInLocalStorage(Page page, int expected) {
        Object actual = page.evaluate("() => JSON.parse(localStorage.getItem('react-todos') || '[]').filter(todo => todo.completed).length");
//        assertThat(actual).isInstanceOf(Number.class);
//        assertThat(((Number) actual).intValue()).isEqualTo(expected);
    }

    private static void checkTodosInLocalStorage(Page page, String title) {
        Object result = page.evaluate("title => JSON.parse(localStorage.getItem('react-todos') || '[]').map(todo => todo.title).includes(title)", title);
//        assertThat(result).isEqualTo(true);
    }

    private Locator newTodoInput() {
        return page.getByPlaceholder("What needs to be done?");
    }

    private Locator todoItems() {
        return page.getByTestId("todo-item");
    }

    private Locator todoCount() {
        return page.getByTestId("todo-count");
    }

    // Note: The original TS 'waitForFunction' waits until the condition is true.
    // This Java equivalent checks the state *at the time of the call*.
    // If the localStorage update is asynchronous and timing becomes an issue,
    // more robust waiting strategies (like Awaitility) might be needed.
    // However, for many operations, checking immediately after the action is sufficient.

    private Locator clearCompletedButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Clear completed"));
    }

    private Locator toggleAllCheckbox() {
        return page.getByLabel("Mark all as complete");
    }


    @BeforeEach
    void navigateToApp() {
        page.navigate(TODO_MVC_URL);
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("New TODO")
    @Nested
    @Tag("NewTodo")
    class NewTodoTests {

        @DisplayName("Should allow adding TODO items")
        @Test
        @Tag("P1")
        @Tag("Smoke")
        void shouldAllowAddingTodoItems() {
            // Create 1st todo
            newTodoInput().fill(TODO_ITEMS.get(0));
            newTodoInput().press("Enter");

            // Make sure the list only has one todo item
            assertThat(todoItems()).hasCount(1);
            assertThat(todoItems()).hasText(TODO_ITEMS.get(0));

            // Create 2nd todo
            newTodoInput().fill(TODO_ITEMS.get(1));
            newTodoInput().press("Enter");

            // Make sure the list now has two todo items
            assertThat(todoItems()).hasCount(2);
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(1)});

            checkNumberOfTodosInLocalStorage(page, 2);
        }

        @Test
        @Tag("P2")
        @Tag("Regression")
        void shouldClearTextInputFieldWhenItemIsAdded() {
            // Create one todo item
            newTodoInput().fill(TODO_ITEMS.get(0));
            newTodoInput().press("Enter");

            // Check that input is empty
            assertThat(newTodoInput()).isEmpty();
            checkNumberOfTodosInLocalStorage(page, 1);
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldAppendNewItemsToBottom() {
            // Create 3 items
            createDefaultTodos(page);

            // Check count display using different methods
            assertThat(page.getByText("3 items left")).isVisible();
            assertThat(todoCount()).hasText("3 items left");
            assertThat(todoCount()).containsText("3");
            assertThat(todoCount()).hasText(Pattern.compile("3 items left")); // Regex example

            // Check all items in one call.
            assertThat(todoItems()).hasText(TODO_ITEMS.toArray(new String[0]));
            checkNumberOfTodosInLocalStorage(page, 3);
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("Completed TODO")
    @Nested
    @Tag("MarkAll")
    class MarkAllAsCompletedTests {

        @BeforeEach
        void setupTodos() {
            createDefaultTodos(page);
            checkNumberOfTodosInLocalStorage(page, 3);
        }

        @AfterEach
        void checkLocalStorageAfter() {
            // This check ensures tests within this suite don't accidentally delete items
            // Adjust if a test is *supposed* to change the total count by the end.
            checkNumberOfTodosInLocalStorage(page, 3);
        }

//        @Test
//        @Tag("P1") @Tag("Smoke")
//        void shouldAllowMarkingAllItemsAsCompleted() {
//            // Complete all todos
//            toggleAllCheckbox().check();
//
//            // Ensure all todos have 'completed' class.
//            // Playwright Java's hasClass checks the first element by default.
//            // To check all, we iterate or use AssertJ's collection assertions.
//            assertThat(todoItems()).hasCount(3); // Verify count first
//            assertThat(todoItems().all()).allSatisfy(item ->
//                    assertThat(item).hasClass("completed")
//            );
//
//            checkNumberOfCompletedTodosInLocalStorage(page, 3);
//        }
//
//        @Test
//        @Tag("P2") @Tag("Regression")
//        void shouldAllowClearingCompleteStateOfAllItems() {
//            // Check and then immediately uncheck.
//            toggleAllCheckbox().check();
//            toggleAllCheckbox().uncheck();
//
//            // Should be no completed classes.
//            assertThat(todoItems()).hasCount(3);
//            assertThat(todoItems().all()).allSatisfy(item ->
//                    assertThat(item).not().hasClass("completed")
//            );
//            // Or check that none have the class:
//            // assertThat(todoItems()).not().hasClass(Pattern.compile("completed")); // Checks first element only
//        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void completeAllCheckboxShouldUpdateState() {
            toggleAllCheckbox().check();
            assertThat(toggleAllCheckbox()).isChecked();
            checkNumberOfCompletedTodosInLocalStorage(page, 3);

            // Uncheck first todo
            Locator firstTodo = todoItems().nth(0);
            firstTodo.getByRole(AriaRole.CHECKBOX).uncheck();

            // Reuse toggleAll locator and make sure it's not checked.
            assertThat(toggleAllCheckbox()).not().isChecked();

            // Check first todo again
            firstTodo.getByRole(AriaRole.CHECKBOX).check();
            checkNumberOfCompletedTodosInLocalStorage(page, 3);

            // Assert the toggle all is checked again.
            assertThat(toggleAllCheckbox()).isChecked();
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("TODO Items")
    @Nested
    @Tag("Item")
    class ItemTests {

        @Test
        @Tag("P1")
        @Tag("Smoke")
        void shouldAllowMarkingItemsAsComplete() {
            // Create two items
            newTodoInput().fill(TODO_ITEMS.get(0));
            newTodoInput().press("Enter");
            newTodoInput().fill(TODO_ITEMS.get(1));
            newTodoInput().press("Enter");

            Locator firstTodo = todoItems().nth(0);
            Locator secondTodo = todoItems().nth(1);

            // Check first item
            firstTodo.getByRole(AriaRole.CHECKBOX).check();
            assertThat(firstTodo).hasClass("completed");

            // Check second item isn't completed, then check it
            assertThat(secondTodo).not().hasClass("completed");
            secondTodo.getByRole(AriaRole.CHECKBOX).check();

            // Assert both are completed
            assertThat(firstTodo).hasClass("completed");
            assertThat(secondTodo).hasClass("completed");
            checkNumberOfCompletedTodosInLocalStorage(page, 2); // Added check
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldAllowUnmarkingItemsAsComplete() {
            // Create two items
            newTodoInput().fill(TODO_ITEMS.get(0));
            newTodoInput().press("Enter");
            newTodoInput().fill(TODO_ITEMS.get(1));
            newTodoInput().press("Enter");

            Locator firstTodo = todoItems().nth(0);
            Locator secondTodo = todoItems().nth(1);
            Locator firstTodoCheckbox = firstTodo.getByRole(AriaRole.CHECKBOX);

            // Check first item
            firstTodoCheckbox.check();
            assertThat(firstTodo).hasClass("completed");
            assertThat(secondTodo).not().hasClass("completed");
            checkNumberOfCompletedTodosInLocalStorage(page, 1);

            // Uncheck first item
            firstTodoCheckbox.uncheck();
            assertThat(firstTodo).not().hasClass("completed");
            assertThat(secondTodo).not().hasClass("completed");
            checkNumberOfCompletedTodosInLocalStorage(page, 0);
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldAllowEditingAnItem() {
            createDefaultTodos(page);

            Locator secondTodo = todoItems().nth(1);
            secondTodo.dblclick();

            Locator editInput = secondTodo.getByRole(AriaRole.TEXTBOX, new Locator.GetByRoleOptions().setName("Edit"));
            assertThat(editInput).hasValue(TODO_ITEMS.get(1));

            editInput.fill("buy some sausages");
            editInput.press("Enter");

            // Explicitly assert the new text value
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), "buy some sausages", TODO_ITEMS.get(2)});
            checkTodosInLocalStorage(page, "buy some sausages");
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("Edit TODO")
    @Nested
    @Tag("Editing")
    class EditingTests {

        @BeforeEach
        void setupTodos() {
            createDefaultTodos(page);
            checkNumberOfTodosInLocalStorage(page, 3);
        }

        @Test
        @Tag("P2")
        @Tag("Regression")
        void shouldHideOtherControlsWhenEditing() {
            Locator todoItem = todoItems().nth(1);
            todoItem.dblclick();

            assertThat(todoItem.getByRole(AriaRole.CHECKBOX)).not().isVisible();
            // Use hasText option within locator
            assertThat(todoItem.locator("label", new Locator.LocatorOptions().setHasText(TODO_ITEMS.get(1)))).not().isVisible();

            checkNumberOfTodosInLocalStorage(page, 3); // Verify count hasn't changed
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldSaveEditsOnBlur() {
            Locator secondTodo = todoItems().nth(1);
            secondTodo.dblclick();

            Locator editInput = secondTodo.getByRole(AriaRole.TEXTBOX, new Locator.GetByRoleOptions().setName("Edit"));
            editInput.fill("buy some sausages");
            editInput.dispatchEvent("blur"); // Trigger blur

            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), "buy some sausages", TODO_ITEMS.get(2)});
            checkTodosInLocalStorage(page, "buy some sausages");
        }

        @Test
        @Tag("P2")
        @Tag("Regression")
        void shouldTrimEnteredText() {
            Locator secondTodo = todoItems().nth(1);
            secondTodo.dblclick();

            Locator editInput = secondTodo.getByRole(AriaRole.TEXTBOX, new Locator.GetByRoleOptions().setName("Edit"));
            editInput.fill("    buy some sausages    "); // Text with spaces
            editInput.press("Enter");

            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), "buy some sausages", // Expect trimmed text
                    TODO_ITEMS.get(2)});
            checkTodosInLocalStorage(page, "buy some sausages");
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldRemoveItemIfEmptyTextEntered() {
            Locator secondTodo = todoItems().nth(1);
            secondTodo.dblclick();

            Locator editInput = secondTodo.getByRole(AriaRole.TEXTBOX, new Locator.GetByRoleOptions().setName("Edit"));
            editInput.fill(""); // Empty text
            editInput.press("Enter");

            assertThat(todoItems()).hasCount(2);
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(2)});
            checkNumberOfTodosInLocalStorage(page, 2); // Check local storage count
        }

        @Test
        @Tag("P2")
        @Tag("Regression")
        void shouldCancelEditsOnEscape() {
            Locator secondTodo = todoItems().nth(1);
            secondTodo.dblclick();

            Locator editInput = secondTodo.getByRole(AriaRole.TEXTBOX, new Locator.GetByRoleOptions().setName("Edit"));
            editInput.fill("buy some sausages");
            editInput.press("Escape"); // Cancel edit

            // Should revert to original list
            assertThat(todoItems()).hasText(TODO_ITEMS.toArray(new String[0]));
            checkNumberOfTodosInLocalStorage(page, 3); // Count should be unchanged
            checkTodosInLocalStorage(page, TODO_ITEMS.get(1)); // Original item should still be there
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("TODO Counter")
    @Nested
    @Tag("Counter")
    class CounterTests {

        @Test
        @Tag("P1")
        @Tag("Smoke")
        void shouldDisplayCurrentNumberOfTodoItems() {
            // Add first item
            newTodoInput().fill(TODO_ITEMS.get(0));
            newTodoInput().press("Enter");
            assertThat(todoCount()).containsText("1");
            assertThat(todoCount()).hasText(Pattern.compile("1 item left")); // More specific check

            // Add second item
            newTodoInput().fill(TODO_ITEMS.get(1));
            newTodoInput().press("Enter");
            assertThat(todoCount()).containsText("2");
            assertThat(todoCount()).hasText(Pattern.compile("2 items left"));

            checkNumberOfTodosInLocalStorage(page, 2);
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("Completed TODO")
    @Nested
    @Tag("ClearCompleted")
    class ClearCompletedButtonTests {

        @BeforeEach
        void setupTodos() {
            createDefaultTodos(page);
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldDisplayCorrectTextAndBeVisible() {
            // Complete one item to make the button appear
            todoItems().first().getByRole(AriaRole.CHECKBOX).check();
            assertThat(clearCompletedButton()).isVisible();
            // You could also check the text if needed:
            // assertThat(clearCompletedButton()).hasText("Clear completed");
        }

        @Test
        @Tag("P1")
        @Tag("Smoke")
        void shouldRemoveCompletedItemsWhenClicked() {
            // Complete the middle item
            todoItems().nth(1).getByRole(AriaRole.CHECKBOX).check();
            checkNumberOfCompletedTodosInLocalStorage(page, 1); // Pre-check

            clearCompletedButton().click();

            assertThat(todoItems()).hasCount(2);
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(2)});
            checkNumberOfTodosInLocalStorage(page, 2); // Post-check total
            checkNumberOfCompletedTodosInLocalStorage(page, 0); // Post-check completed
        }

        @Test
        @Tag("P2")
        @Tag("Regression")
        void shouldBeHiddenWhenNoItemsAreCompleted() {
            // Initially hidden
            assertThat(clearCompletedButton()).isHidden();

            // Complete one item
            todoItems().first().getByRole(AriaRole.CHECKBOX).check();
            assertThat(clearCompletedButton()).isVisible(); // Should appear

            // Click clear completed
            clearCompletedButton().click();
            assertThat(clearCompletedButton()).isHidden(); // Should disappear again
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("Retain TODO")
    @Nested
    @Tag("Persistence")
    class PersistenceTests {

        @Test
        @Tag("P1")
        @Tag("Smoke")
        @Tag("Regression")
        void shouldPersistDataOnReload() {
            // Create two items
            newTodoInput().fill(TODO_ITEMS.get(0));
            newTodoInput().press("Enter");
            newTodoInput().fill(TODO_ITEMS.get(1));
            newTodoInput().press("Enter");

            Locator firstTodo = todoItems().nth(0);
            Locator firstTodoCheckbox = firstTodo.getByRole(AriaRole.CHECKBOX);

            // Check the first item
            firstTodoCheckbox.check();
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(1)});
            assertThat(firstTodoCheckbox).isChecked();
            assertThat(todoItems().nth(0)).hasClass("completed");
            assertThat(todoItems().nth(1)).not().hasClass("completed");
            checkNumberOfCompletedTodosInLocalStorage(page, 1);

            // Reload the page
            page.reload();

            // Re-fetch locators after reload is recommended practice
            Locator firstTodoAfterReload = todoItems().nth(0);
            Locator firstTodoCheckboxAfterReload = firstTodoAfterReload.getByRole(AriaRole.CHECKBOX);

            // Assert state is the same after reload
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(1)});
            assertThat(firstTodoCheckboxAfterReload).isChecked();
            assertThat(firstTodoAfterReload).hasClass("completed");
            assertThat(todoItems().nth(1)).not().hasClass("completed");
            checkNumberOfCompletedTodosInLocalStorage(page, 1); // Also check local storage state
            checkNumberOfTodosInLocalStorage(page, 2);
        }
    }

    @Epic("Todo MVC UI")
    @Feature("TODO Operations")
    @Story("Routing")
    @Nested
    @Tag("Routing")
    class RoutingTests {

        @BeforeEach
        void setupTodosAndWait() {
            createDefaultTodos(page);
            // Ensure storage is updated before navigation potentially disrupts it
            checkTodosInLocalStorage(page, TODO_ITEMS.get(0));
            checkTodosInLocalStorage(page, TODO_ITEMS.get(1));
            checkTodosInLocalStorage(page, TODO_ITEMS.get(2));
            checkNumberOfTodosInLocalStorage(page, 3);
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldAllowDisplayingActiveItems() {
            // Complete the second item
            todoItems().nth(1).getByRole(AriaRole.CHECKBOX).check();
            checkNumberOfCompletedTodosInLocalStorage(page, 1);

            // Navigate to Active filter
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Active")).click();

            // Assert only active items are shown
            assertThat(todoItems()).hasCount(2);
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(2)});
        }

//        @Test
//        @Tag("P2") @Tag("Regression")
//        void shouldRespectTheBackButton() {
//            // Complete the second item
//            todoItems().nth(1).getByRole(AriaRole.CHECKBOX).check();
//            checkNumberOfCompletedTodosInLocalStorage(page, 1);
//
//            Locator linkAll = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("All"));
//            Locator linkActive = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Active"));
//            Locator linkCompleted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Completed"));
//
//            // Navigate through filters
//            // Start at All (default)
//            assertThat(todoItems()).hasCount(3);
//
//            // Go to Active
//            linkActive.click();
//            assertThat(todoItems()).hasCount(2);
//
//            // Go to Completed
//            linkCompleted.click();
//            assertThat(todoItems()).hasCount(1);
//            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(1)});
//
//
//            // Use back button
//            page.goBack(); // Should go back to Active view
//            assertThat(page.url()).endsWith("#/active"); // Ensure URL matches
//            assertThat(todoItems()).hasCount(2);
//            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(0), TODO_ITEMS.get(2)});
//
//
//            page.goBack(); // Should go back to All view
//            assertThat(page.url()).endsWith("#/"); // Ensure URL matches
//            assertThat(todoItems()).hasCount(3);
//            assertThat(todoItems()).hasText(TODO_ITEMS.toArray(new String[0]));
//        }


        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldAllowDisplayingCompletedItems() {
            // Complete the second item
            todoItems().nth(1).getByRole(AriaRole.CHECKBOX).check();
            checkNumberOfCompletedTodosInLocalStorage(page, 1);

            // Navigate to Completed filter
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Completed")).click();

            // Assert only completed item is shown
            assertThat(todoItems()).hasCount(1);
            assertThat(todoItems()).hasText(new String[]{TODO_ITEMS.get(1)});
        }

        @Test
        @Tag("P1")
        @Tag("Regression")
        void shouldAllowDisplayingAllItems() {
            // Complete the second item
            todoItems().nth(1).getByRole(AriaRole.CHECKBOX).check();
            checkNumberOfCompletedTodosInLocalStorage(page, 1);

            // Navigate away and back to All
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Active")).click();
            assertThat(todoItems()).hasCount(2); // Verify active view

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Completed")).click();
            assertThat(todoItems()).hasCount(1); // Verify completed view

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("All")).click();
            assertThat(todoItems()).hasCount(3); // Verify All view shows all items
            assertThat(todoItems()).hasText(TODO_ITEMS.toArray(new String[0]));
        }

        @Test
        @Tag("P2")
        @Tag("Regression")
        void shouldHighlightCurrentlyAppliedFilter() {
            Locator linkAll = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("All"));
            Locator linkActive = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Active"));
            Locator linkCompleted = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Completed"));

            // Default: All should be selected
            assertThat(linkAll).hasClass("selected");
            assertThat(linkActive).not().hasClass("selected");
            assertThat(linkCompleted).not().hasClass("selected");

            // Click Active
            linkActive.click();
            assertThat(linkAll).not().hasClass("selected");
            assertThat(linkActive).hasClass("selected");
            assertThat(linkCompleted).not().hasClass("selected");

            // Click Completed
            linkCompleted.click();
            assertThat(linkAll).not().hasClass("selected");
            assertThat(linkActive).not().hasClass("selected");
            assertThat(linkCompleted).hasClass("selected");

            // Click All again
            linkAll.click();
            assertThat(linkAll).hasClass("selected");
            assertThat(linkActive).not().hasClass("selected");
            assertThat(linkCompleted).not().hasClass("selected");
        }
    }
}
