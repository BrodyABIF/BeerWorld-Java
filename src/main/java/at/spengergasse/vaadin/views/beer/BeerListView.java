package at.spengergasse.vaadin.views.beer;


import at.spengergasse.vaadin.Application;
import at.spengergasse.vaadin.domain.Beer;
import at.spengergasse.vaadin.domain.Company;
import at.spengergasse.vaadin.exception.BeerException;
import at.spengergasse.vaadin.services.BeerRepository;
import at.spengergasse.vaadin.services.BeerService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.ButtonVariant;



import java.util.Set;

import static at.spengergasse.vaadin.views.beer.BeerEditView.BEER_ID;


@PageTitle(value = "BeerWood Sorten")
@Route(value = "beer/list", layout = MainLayout.class)
public class BeerListView extends VerticalLayout {

    private final BeerRepository repository;
    private final Grid<Beer> beerGrid = new Grid<>(Beer.class, false);
    private final BeerService service;
    private final Notification notification = new Notification();


    public BeerListView(@Autowired BeerRepository beerRepository, BeerService beerService) {
        addClassName("background-image-view");


        this.repository = beerRepository;
        this.service = beerService;

        add(new H3("Sorten Übersicht"), beerGrid);

        Button deleteSelected = new Button("Auswahl löschen", LineAwesomeIcon.DUMPSTER_SOLID.create(), e -> onDeleteSelected());
        deleteSelected.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout toolbar = new HorizontalLayout( deleteSelected );
        deleteSelected.setEnabled(false);

        beerGrid.setItems(query -> repository.findAll(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        beerGrid.addColumn(Beer::getSorte).setHeader("Sorte").setFlexGrow(100);
        beerGrid.addColumn(Beer::getStammwuerze).setHeader("Stammwuerze").setFlexGrow(100);
        beerGrid.addColumn(Beer::getAlkoholanteil).setHeader("Alkoholgehalt").setFlexGrow(100);
        beerGrid.addColumn(Beer::getBraujahr).setHeader("Braujahr").setFlexGrow(100);
        beerGrid.addColumn(Beer::getPreis).setHeader("Preis").setFlexGrow(100);

        beerGrid.setSelectionMode( Grid.SelectionMode.MULTI );

        beerGrid.addComponentColumn(new ValueProvider<Beer, Component>() {
            @Override
            public Component apply(Beer beer) {
                Button show = new Button("Anzeigen", LineAwesomeIcon.FOLDER_OPEN.create(), e -> onOpen(beer));
                Button edit = new Button("Edit", LineAwesomeIcon.EDIT.create() ,e -> onEdit(beer));
                Button delete = new Button("Delete", LineAwesomeIcon.TRASH_SOLID.create(), e -> onDelete(beer));
                delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

                show.setWidth("120px");
                edit.setWidth("80px");
                delete.setWidth("100px");

                HorizontalLayout buttonLayout = new HorizontalLayout(show, edit, delete);
                buttonLayout.setSpacing(true);

                return buttonLayout;
            }

        }).setHeader("Aktions").setWidth("600px").setTextAlign(ColumnTextAlign.CENTER);


        beerGrid.addSelectionListener(new SelectionListener<Grid<Beer>, Beer>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<Beer>, Beer> event) {
                Set<Beer> allSelectedItems = event.getAllSelectedItems();

                deleteSelected.setEnabled(allSelectedItems != null && allSelectedItems.size() > 0);
            }
        });


        loadData();
        add(beerGrid, toolbar);
    }


    private void loadData() {
        beerGrid.setItems(query -> repository.findAll( VaadinSpringDataHelpers.toSpringPageRequest( query ) ).stream());
    }

    private void onOpen(Beer beer) {
        Dialog dialog2 = new Dialog();
        dialog2.setWidthFull();
        final Button DialogCancel = new Button("Abbrechen", e -> dialog2.close());

        Grid grid = new Grid(Company.class, true);
        grid.addComponentColumn(new ValueProvider<Company, Component>() {
            @Override
            public Component apply(Company company) {
                return new Button("Company entfernen", LineAwesomeIcon.TIMES_SOLID.create(), e -> onDelete(company));
            }
            private void onDelete(Company company) {
                service.deleteFromCompany(beer);
                notification.setText("\""+company.getName()+"\" wurde erfolgreich von \""+ beer.getSorte()+ "\" entfernt");
                notification.open();
                loadData();
                dialog2.close();
            }
        });
        dialog2.add(grid);
        dialog2.add(new HorizontalLayout(DialogCancel));
        grid.setItems(service.findCompanyByBeer(beer));
        dialog2.open();
    }

    private void onEdit(Beer beer) {
        getUI().ifPresent(ui -> ui.navigate(BeerEditView.class, new RouteParam( BEER_ID, beer.getId()) ));
    }

    private void onDelete(Beer beer){
        try {
            repository.delete(beer);
            Application.info("Sorte \"" + beer.getSorte() +"\" wurde erfogreich gelöscht");
            loadData();
        } catch (BeerException e) {
            Application.error(e.getMessage());
        } catch (Exception e){
            Application.error(e.getMessage());
        }
    }

    private void onDeleteSelected() {
        try {
            Set<Beer> selectedItems = beerGrid.getSelectedItems();
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

