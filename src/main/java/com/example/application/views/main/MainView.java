package com.example.application.views.main;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.vaadin.addons.apollonav.ApolloNav;
import org.vaadin.addons.apollonav.ApolloNavItem;

import com.example.application.components.ThemeButton;
import com.example.application.security.SecurityConfig;

import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * The main view is a top-level placeholder for other views.
 */
@UIScope
@org.springframework.stereotype.Component
public class MainView extends AppLayout {

    private H1 viewTitle;
    private Avatar avatar;
    private UserInfo localUser;
    private Span userLabel;

    public MainView() {
        String username = setCurrentUser();
        localUser = new UserInfo(username, username,
                "https://i.pravatar.cc/150?img=54");
        UI.getCurrent().getElement().setAttribute("theme", "light-contrast");
        setPrimarySection(Section.DRAWER);
        addToNavbar(false, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private String setCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            System.err.println("No context when checking user");
            return "";
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            System.err
                    .println("No authentication in context when checking user");
            return "";
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            System.err.println("No principal when checking user");
            return "";
        }
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public UserInfo getlocalUser() {
        return localUser;
    }

    private Component createHeaderContent() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames("text-secondary");
        drawerToggle.setThemeName("contrast");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l", "flex-auto");

        Icon hashIcon = VaadinIcon.HASH.create();
        hashIcon.addClassNames("text-s", "pe-s", "text-secondary");

        Header layout = new Header();
        layout.addClassName("main-layout-header");
        layout.addClassNames("bg-base", "border-b", "border-contrast-10",
                "box-border", "flex", "h-xl", "px-m", "items-center", "w-full");

        layout.setWidthFull();
        ThemeButton button = new ThemeButton();

        layout.add(new DrawerToggle(), hashIcon, viewTitle, button);
        return layout;
    }

    private Component createDrawerContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames("main-layout-drawer");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);

        Header header = new Header();
        header.addClassNames("flex", "items-center", "h-xl", "px-m", "space-s",
                "border-b", "border-contrast-10", "w-full", "box-border",
                "bg-contrast-10");
        header.addClassNames("main-layout-drawer-header");
        Icon icon = VaadinIcon.COMMENTS_O.create();
        icon.addClassNames("text-m");
        H2 text = new H2("Vabber");
        text.addClassNames("text-m", "m-0");
        header.add(icon, text);

        ApolloNav vaadinNav = new ApolloNav("Channels");
        vaadinNav.addClassNames("main-layout-drawer-nav");
        vaadinNav.addClassNames("w-full", "px-s", "box-border",
                "bg-contrast-10");
        vaadinNav.setItems(new ApolloNavItem("c/general", "general", "hash"),
                new ApolloNavItem("c/announcements", "announcements", "hash",
                        2),
                new ApolloNavItem("c/music", "music", "hash", 5),
                new ApolloNavItem("c/community", "community", "hash"),
                new ApolloNavItem("about", "About", "question", null,
                        Arrays.asList(
                                new ApolloNavItem("about", "Instructions",
                                        "newspaper", 4),
                                new ApolloNavItem("faq", "FAQ", "movie"))));

        Div logoutDiv = new Div();
        logoutDiv.addClassNames("w-full", "px-s", "box-border",
                "bg-contrast-10", "m-0");
        Footer footer = createMenuFooter();
        layout.add(header, vaadinNav, footer); // TODO: menu,
        layout.expand(vaadinNav);
        return layout;
    }

    public void logout() {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(false);
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
        UI.getCurrent().getPage()
                .setLocation(SecurityConfig.LOGOUT_SUCCESS_URL);
    }

    private Footer createMenuFooter() {
        avatar = new Avatar(localUser.getName(), localUser.getImage());
        avatar.addClassNames("mr-xs pointer");

        userLabel = new Span(localUser.getName());
        userLabel.addClassNames("font-medium", "text-s", "text-secondary");
        userLabel.addClassNames("flex-grow");
        Button logout = new Button("Log out", e -> logout());
        logout.addThemeVariants(ButtonVariant.LUMO_CONTRAST,
                ButtonVariant.LUMO_TERTIARY);
        logout.addClassNames("text-secondary", "text-s");

        Footer footer = new Footer(avatar, userLabel, logout);
        footer.addClassNames("main-layout-drawer-footer");
        footer.addClassNames("flex", "items-center", "px-m", "pointer", "h-xl",
                "bg-contrast-20", "w-full", "box-border");
        footer.addClickListener(event -> {
            // footer.getUI().ifPresent(ui ->
            // ui.navigate(YourProfileView.class));
        });

        return footer;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        setViewTitle(getCurrentPageTitle());
    }

    public void setViewTitle(String title) {
        viewTitle.setText(title);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass()
                .getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    public static MainView getInstance() {
        return (MainView) UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == MainView.class)
                .findFirst().orElse(null);
    }
}
