package com.neudesic.qa.core;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Base64;

public class ScreenShootOnFailureExtension implements InvocationInterceptor {
    private static final Logger LOGGER = LogManager.getLogger(ScreenShootOnFailureExtension.class);
    private static final ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();

    public static void setPage(Page page) {
        threadLocalPage.set(page);
 }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        try {
            invocation.proceed();
        } catch (Throwable cause) {
            Page page = threadLocalPage.get();
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")).setFullPage(true));
            LOGGER.error(
                    "RP_MESSAGE#BASE64#{}#{}",
                    Base64.getEncoder().encodeToString(screenshot),
                    "Error screenshot"
            );
            throw cause;
        }
    }
}
