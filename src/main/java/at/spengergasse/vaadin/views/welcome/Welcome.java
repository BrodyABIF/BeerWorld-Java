package at.spengergasse.vaadin.views.welcome;

import at.spengergasse.vaadin.domain.Company;
import at.spengergasse.vaadin.services.CompanyRepository;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Willkommen")
@Route(value = "welcome", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class Welcome extends VerticalLayout {

    public Welcome(@Autowired CompanyRepository companyRepository) {
        Grid<Company> grid = new Grid<>(Company.class, true);
        grid.setItems( query -> companyRepository.findAll( VaadinSpringDataHelpers.toSpringPageRequest( query ) ).stream());
        add(new H3("Firmenliste"), grid);
    }
}
