package at.spengergasse.vaadin.views.beer;

import at.spengergasse.vaadin.domain.Beer;
import at.spengergasse.vaadin.services.BeerService;
import at.spengergasse.vaadin.views.MainLayout;
import at.spengergasse.vaadin.views.manufacture.Manufacture;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.Binder;

import java.util.Optional;



@PageTitle(value = "Sorte editieren")
@Route(value = "beer/:" + BeerEditView.BEER_ID + "?/edit", layout = MainLayout.class)

public class BeerEditView extends VerticalLayout implements BeforeEnterObserver {

    public static final String BEER_ID = "beerId";

    private final BeerService service;
    private final Binder<Beer> binder = new Binder<>(Beer.class, false);

    private final TextField sorte = new TextField("Sorte");
    private final IntegerField stammwuerze = new IntegerField("Stammwürze");
    private final NumberField alkoholAnteil = new NumberField("Alkoholgehalt");
    private final IntegerField brauJahr = new IntegerField("Braujahr");
    private final NumberField preis = new NumberField("Preis");

    public final Button cancel = new Button("Cancel", LineAwesomeIcon.TIMES_SOLID.create(), e -> onCancel());
    public final Button save = new Button("Speichern", LineAwesomeIcon.SAVE.create(), e -> onSave());


    public BeerEditView(@Autowired BeerService service) {
        this.service = service;
        init();
    }

    private void init() {
        FormLayout formLayout = new FormLayout();
        formLayout.add();
        binder.bindInstanceFields(this);

        setWidthFull();

        sorte.setWidth("450px");
        stammwuerze.setWidth("150px");
        alkoholAnteil.setWidth("150px");
        brauJahr.setWidth("150px");
        preis.setWidth("450px");

        binder.forField(sorte)
                .asRequired("Sorte darf nicht leer sein")
                .bind(Beer::getSorte, Beer::setSorte);

        binder.forField(stammwuerze)
                .withValidator(value -> value != null && value >= 1,"Stammwürze muss mindestens 1 sein")
                .bind(Beer::getStammwuerze, Beer::setStammwuerze);

        binder.forField(alkoholAnteil)
                .withValidator(value -> value != null && value >= 0.1d,"Alkoholgehalt muss mindestens 0.01 % sein")
                .bind(Beer::getAlkoholanteil, Beer::setAlkoholanteil);

        binder.forField(brauJahr)
                .withValidator(value -> value != null && value >= 2000,"Braujahr muss mindestens 2000 sein")
                .bind(Beer::getBraujahr, Beer::setBraujahr);

        binder.forField(preis)
                .withValidator(value -> value != null && value >= 0.10d,"Preis muss mindestens 0.10 € sein")
                .bind(Beer::getPreis, Beer::setPreis);


        preis.setStepButtonsVisible(true);
        preis.setStep(0.01d);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        binder.bindInstanceFields(this);
        binder.setBean(new Beer());

        VerticalLayout centeredLayout = new VerticalLayout();
        centeredLayout.setAlignItems(Alignment.CENTER);
        centeredLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeredLayout.setSizeFull();

        centeredLayout.add(sorte, new HorizontalLayout(stammwuerze, alkoholAnteil, brauJahr) , preis, new HorizontalLayout(save, cancel));

        add(formLayout, centeredLayout);
    }


    private void onCancel() {
        getUI().ifPresent(ui -> ui.navigate(BeerListView.class));
    }

    private void onSave() {
        try {
            if (binder.validate().isOk()) {
                service.saveBeer(binder.getBean());
                getUI().ifPresent(ui -> ui.navigate(Manufacture.class));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setBeer(Beer beer) {
        if (beer == null) {
            beer = new Beer();
        }
        binder.setBean(beer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> optional = event.getRouteParameters().getLong(BEER_ID);
        optional.ifPresentOrElse(aLong -> setBeer( service.findBeerById(aLong) ), () -> setBeer(null));
    }
}
