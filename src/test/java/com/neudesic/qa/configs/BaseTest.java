package com.neudesic.qa.configs;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.neudesic.qa.core.ScreenShootOnFailureExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ScreenShootOnFailureExtension.class)
@UsePlaywright(ExecutionOptions.CustomOptions.class)
public abstract class BaseTest {
    protected Page page;
    private static final ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);


    @BeforeEach
    void setupTests(Page page) {
        LOGGER.info("----------- test start => {}", Thread.currentThread().getName());
        threadLocalPage.set(page);
        this.page = page;
        ScreenShootOnFailureExtension.setPage(page);
    }

    @AfterEach
    void cleanupTests() {
        threadLocalPage.remove();
        LOGGER.info("----------- test end => {}", Thread.currentThread().getName());
    }

    protected Page getPage() {
        return threadLocalPage.get();
    }
}
