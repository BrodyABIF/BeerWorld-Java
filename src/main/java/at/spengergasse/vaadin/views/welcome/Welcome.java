package at.spengergasse.vaadin.views.welcome;

import at.spengergasse.vaadin.domain.Company;
import at.spengergasse.vaadin.domain.Motorcycle;
import at.spengergasse.vaadin.services.CompanyRepository;
import at.spengergasse.vaadin.services.MotoService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.component.dialog.Dialog;

@PageTitle("Willkommen bei TÖFFINATOR")
@Route(value = "welcome", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class Welcome extends VerticalLayout {

    private final Dialog dialog = new Dialog();

    private Company currentCompany = null;

    private final Button dialogSave = new Button("Speichern", e -> onSave());
    private final Button dialogCancel = new Button("Abbrechen", buttonClickEvent -> dialog.close());

    private final ComboBox<Motorcycle> comboBox = new ComboBox<>("Motorrad auswählen");
    private final MotoService motoService;

    public Welcome(@Autowired CompanyRepository companyRepository, @Autowired MotoService motoService) {
        this.motoService = motoService;
        Grid<Company> grid = new Grid<>(Company.class, false);
        grid.setItems( query -> companyRepository.findAll( VaadinSpringDataHelpers.toSpringPageRequest( query ) ).stream());
        grid.addColumn(Company::getName).setHeader("Name");
        add(new H3("Filialen Übersicht"), grid);

        grid.addComponentColumn(new ValueProvider<Company, Component>() {
            @Override
            public Component apply(Company company) {
                return new Button("Motorrad hinzufügen", LineAwesomeIcon.MOTORCYCLE_SOLID.create(), e -> onClick(company));
            }
        });

        dialog.add(comboBox);
        dialog.add(new HorizontalLayout(dialogSave, dialogCancel));

        add(dialog);
    }


    private void onSave() {

        motoService.hire(currentCompany.getId(), comboBox.getValue().getId());
        dialog.close();

    }


    private void onClick(Company company) {
        comboBox.setItems(motoService.findFreeMoto());
        comboBox.setItemLabelGenerator(moto -> moto.getBezeichnung());

        this.currentCompany = company;

        dialog.open();
    }
}
