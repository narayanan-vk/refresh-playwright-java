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
    private Page page;
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);


    @BeforeEach
    void setupTests(Page page) {
        LOGGER.info("----------- test start => {}", Thread.currentThread().getName());
        this.page = page;
        ScreenShootOnFailureExtension.setPage(page);
    }

    @AfterEach
    void cleanupTests() {
        this.page.close();
        LOGGER.info("----------- test end => {}", Thread.currentThread().getName());
    }

    protected Page getPage() {
        return this.page;
    }
}
