package com.neudesic.qa.configs;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

public class ExecutionOptions {

    public static class CustomOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            Options options = new Options()
                    .setHeadless(ConfigManager.isHeadless())
                    .setBrowserName(ConfigManager.getBrowser())
                    .setContextOptions(new Browser.NewContextOptions().setBaseURL(ConfigManager.getBaseUrl()))
                    .setTrace(ConfigManager.getTrace());

            if (!ConfigManager.isCi()) {
                options.setWsEndpoint(ConfigManager.getServer());
            }

            return options;
        }
    }
}

