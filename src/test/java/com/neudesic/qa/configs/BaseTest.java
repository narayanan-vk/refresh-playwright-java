package com.neudesic.qa.configs;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.neudesic.qa.core.ScreenShootOnFailureExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ScreenShootOnFailureExtension.class)
@UsePlaywright(ExecutionOptions.CustomOptions.class)
public abstract class BaseTest {
    protected Page page;
    private static final ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();


    @BeforeEach
    void setupTests(Page page) {
        threadLocalPage.set(page);
        this.page = page;
        ScreenShootOnFailureExtension.setPage(page);
    }

    @AfterEach
    void cleanupTests() {
        threadLocalPage.remove();
    }

    protected Page getPage() {
        Page threadPage = threadLocalPage.get();
        return threadPage != null ? threadPage : page;
    }
}
