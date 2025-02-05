package at.spengergasse.vaadin.views.manufacture;

import at.spengergasse.vaadin.Application;
import at.spengergasse.vaadin.domain.Company;
import at.spengergasse.vaadin.domain.Beer;
import at.spengergasse.vaadin.exception.BeerException;
import at.spengergasse.vaadin.services.CompanyRepository;
import at.spengergasse.vaadin.services.BeerService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.Set;

import static at.spengergasse.vaadin.views.manufacture.ManufactureEditView.COMPANY_ID;

@PageTitle("BeerWood Manufacture")
@Route(value = "manufacture", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class Manufacture extends VerticalLayout {

    private final ComboBox<Beer> comboBox = new ComboBox<>("Beer auswählen");
    private final BeerService beerService;
    private final CompanyRepository repository;
    private final Grid<Company> grid = new Grid<>(Company.class, false);
    private final Notification notification = new Notification();

    private final Dialog dialog = new Dialog();
    private final Button DialogSave = new Button("Speichern", e -> onSaveDialog());
    private final Button DialogCancel = new Button("Abbrechen", e -> dialog.close());

    private Company currentCompany = null;


    public Manufacture(@Autowired CompanyRepository companyRepository, @Autowired BeerService beerService) {
        this.beerService = beerService;
        this.repository = companyRepository;

        add(new H3("Location Übersicht"), grid);

        Button deleteSelected = new Button("Auswahl löschen", LineAwesomeIcon.DUMPSTER_SOLID.create(), e -> onDeleteSelected());
        deleteSelected.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout toolbar = new HorizontalLayout(deleteSelected);
        deleteSelected.setEnabled(false);


        grid.setItems(query -> companyRepository.findAll(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addColumn(Company::getName).setHeader("Name").setFlexGrow(100);
        grid.addColumn(Company::getOrt).setHeader("Ort").setFlexGrow(100);
        grid.addColumn(Company::getPlz).setHeader("Postleitzahl").setFlexGrow(100);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addComponentColumn(new ValueProvider<Company, Component>() {
            @Override
            public Component apply(Company company) {
                Button add = new Button("Beer hinzufügen", LineAwesomeIcon.BEER_SOLID.create(), e -> onClick(company));
                Button show = new Button("Anzeigen", LineAwesomeIcon.FOLDER_OPEN.create(), e -> onOpen(company));
                Button edit = new Button("Edit", LineAwesomeIcon.EDIT.create() ,e -> onEdit(company));
                Button delete = new Button("Delete", LineAwesomeIcon.TRASH_SOLID.create(), e -> onDelete(company));
                delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

                add.setWidth("200");
                show.setWidth("120px");
                edit.setWidth("80px");
                delete.setWidth("100px");

                HorizontalLayout buttonLayout = new HorizontalLayout(add, show, edit, delete);
                buttonLayout.setSpacing(true);

                return buttonLayout;
            }

        }).setHeader("Aktions").setWidth("600px").setTextAlign(ColumnTextAlign.CENTER);



        grid.addSelectionListener(new SelectionListener<Grid<Company>, Company>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<Company>, Company> event) {
                Set<Company> allSelectedItems = event.getAllSelectedItems();

                deleteSelected.setEnabled(allSelectedItems != null && allSelectedItems.size() > 0);
            }
        });



        loadData();
        dialog.add(comboBox);
        dialog.add(new HorizontalLayout(DialogSave, DialogCancel));
        add(grid, dialog, toolbar);
    }


    private void loadData() {
        grid.setItems(query -> repository.findAll(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    }

    private void onSaveDialog() {
        beerService.hire(currentCompany.getId(), comboBox.getValue().getId());
        dialog.close();
    }

    private void onClick(Company company) {
        comboBox.setItems(beerService.findFreeBeer());
        comboBox.setItemLabelGenerator(beer -> beer.getSorte());

        this.currentCompany = company;

        dialog.open();
    }

    private void onOpen(Company company) {
        Dialog dialog1 = new Dialog();
        dialog1.setWidthFull();
        final Button DialogCancel = new Button("Abbrechen", e -> dialog1.close());

        Grid grid = new Grid<>(Beer.class, true);
        grid.addComponentColumn(new ValueProvider<Beer, Component>() {
            @Override
            public Component apply(Beer beer) {
                return new Button("Beer entfernen", LineAwesomeIcon.TIMES_SOLID.create(), e -> onDelete(beer));
            }
            private void onDelete(Beer beer) {
                beerService.deleteFromCompany(beer);
                grid.setItems(beerService.findBeerByCompany(company));
                notification.setText("\""+beer.getSorte()+"\" wurde erfolgreich von \""+ company.getName()+ "\" entfernt");
                notification.open();
            }
        });
        dialog1.add(grid);
        dialog1.add(new HorizontalLayout(DialogCancel));
        grid.setItems(beerService.findBeerByCompany(company));
        dialog1.open();
    }

    private void onEdit(Company company) {
        getUI().ifPresent(ui -> ui.navigate(ManufactureEditView.class, new RouteParam( COMPANY_ID, company.getId()) ));
    }

    private void onDelete(Company company) {
        try {
            repository.delete(company);
            Application.info("Manufacture \"" + company.getName() + "\" wurde erfogreich gelöscht");
            loadData();
        } catch (BeerException e) {
            Application.error(e.getMessage());
        } catch (Exception e) {
            Application.error(e.getMessage());
        }
    }

    private void onDeleteSelected() {
        try {
            Set<Company> selectedItems = grid.getSelectedItems();
            repository.deleteAll(selectedItems);
            notification.setText("Auswahl wurde gelöscht");
            notification.open();
            loadData();
        } catch (Exception e) {
            notification.setText("Fehler beim löschen");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }
}
