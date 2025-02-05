package at.spengergasse.vaadin.views;

import at.spengergasse.vaadin.views.beer.BeerEditView;
import at.spengergasse.vaadin.views.beer.BeerListView;
import at.spengergasse.vaadin.views.manufacture.Manufacture;
import at.spengergasse.vaadin.views.manufacture.ManufactureEditView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("BeerWood");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Manufacture", Manufacture.class, LineAwesomeIcon.BEER_SOLID.create()));
        nav.addItem(new SideNavItem("Manufacture anlegen", ManufactureEditView.class, LineAwesomeIcon.HARD_HAT_SOLID.create()));
        nav.addItem(new SideNavItem("Sorte anlegen", BeerEditView.class, LineAwesomeIcon.MICROSCOPE_SOLID.create()));
        nav.addItem(new SideNavItem("Sorten Übersicht", BeerListView.class, LineAwesomeIcon.CLIPBOARD_LIST_SOLID.create()));
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        Component content = getContent();
        if(content instanceof HasDynamicTitle dynamicTitle) {
            return dynamicTitle.getPageTitle();
        }
        PageTitle title = content.getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
