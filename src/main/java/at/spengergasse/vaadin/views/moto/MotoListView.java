package at.spengergasse.vaadin.views.moto;


import at.spengergasse.vaadin.domain.Motorcycle;
import at.spengergasse.vaadin.services.MotorcycleRepository;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;

import java.util.Set;

@PageTitle(value = "Model Übersicht")
@Route(value = "moto/list", layout = MainLayout.class)
public class MotoListView extends VerticalLayout {

    private final MotorcycleRepository motoRepository;
    private final Grid<Motorcycle> motoGrid = new Grid<>(Motorcycle.class, false);


    public MotoListView(@Autowired MotorcycleRepository motorcycleRepository) {
        this.motoRepository = motorcycleRepository;

        Button deleteSelected = new Button("Auswahl löschen", LineAwesomeIcon.TRASH_SOLID.create(), e -> onDeleteSelected());
        HorizontalLayout toolbar = new HorizontalLayout( deleteSelected );
        deleteSelected.setEnabled(false);

        motoGrid.addColumn(Motorcycle::getBezeichnung).setHeader("Bezeichnung").setFlexGrow(100);

        motoGrid.addColumn(Motorcycle::getHubraum).setHeader("Hubraum").setFlexGrow(100);
        motoGrid.addColumn(Motorcycle::getLeistung).setHeader("Leistung").setFlexGrow(100);
        motoGrid.addColumn(Motorcycle::getDrehmoment).setHeader("Drehmoment").setFlexGrow(100);
        motoGrid.addColumn(Motorcycle::getPreis).setHeader("Preis").setFlexGrow(100);

        motoGrid.setSelectionMode( Grid.SelectionMode.MULTI );

        motoGrid.addSelectionListener(new SelectionListener<Grid<Motorcycle>, Motorcycle>() {
                                          @Override
                                          public void selectionChange(SelectionEvent<Grid<Motorcycle>, Motorcycle> event) {

                                              Set<Motorcycle> allSelectedItems = event.getAllSelectedItems();

                                              deleteSelected.setEnabled(allSelectedItems != null && allSelectedItems.size() > 0);
                                          }
                                      });


        loadData();
        add(motoGrid, toolbar);
    }


    private void onDeleteSelected() {
        try {
            Set<Motorcycle> selectedItems = motoGrid.getSelectedItems();
            motoRepository.deleteAll(selectedItems);
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        motoGrid.setItems( query -> motoRepository.findAll( VaadinSpringDataHelpers.toSpringPageRequest( query ) ).stream());
    }



}

