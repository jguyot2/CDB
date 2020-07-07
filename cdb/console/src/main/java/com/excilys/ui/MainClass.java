package com.excilys.ui;

import com.excilys.ui.config.UiConfig;

public class MainClass {
    public static void main(String... args) {
        CLInterface f = UiConfig.getContext().getBean(CLInterface.class);
        f.start();
    }
}
