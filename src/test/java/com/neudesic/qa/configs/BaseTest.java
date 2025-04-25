package com.neudesic.qa.configs;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.BeforeEach;

@UsePlaywright(ExecutionOptions.CustomOptions.class)
public abstract class BaseTest {
    public Page page;

    @BeforeEach
    void setupTests(Page page) {
        this.page = page;
    }
}
