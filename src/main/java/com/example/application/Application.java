package com.example.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "jabber")
@PWA(name = "Jabber", shortName = "Jabber", offlineResources = {
        "images/logo.png" })
@Push
public class Application extends SpringBootServletInitializer
        implements AppShellConfigurator {

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(
                SpringApplication.run(Application.class, args));
    }

}
