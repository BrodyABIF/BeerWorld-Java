package at.spengergasse.vaadin.views.moto;


import at.spengergasse.vaadin.domain.Motorcycle;
import at.spengergasse.vaadin.services.MotoService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle(value = "Model anlegen")
@Route(value = "moto/create", layout = MainLayout.class)
public class MotoCreateView extends VerticalLayout {

    private final MotoService motoService;

    private final TextField bezeichnung = new TextField("Bezeichnung");
    private final IntegerField hubraum = new IntegerField("Hubraum");
    private final IntegerField leistung = new IntegerField("Leistung");
    private final IntegerField drehmoment = new IntegerField("Drehmoment");
    private final IntegerField preis = new IntegerField("Preis");

    private final Motorcycle newMotorcycle = new Motorcycle();
    private final Binder<Motorcycle> binder = new Binder<>(Motorcycle.class, false);

    private final Button cancel = new Button("Cancel", c -> onCancel());
    private final Button save = new Button("Save", s -> onSave());

    public MotoCreateView(@Autowired MotoService motoService) {
        this.motoService = motoService;
        init();


    }

    private void init() {
        setWidthFull();

        bezeichnung.setWidthFull();

        hubraum.setWidthFull();
        leistung.setWidthFull();
        drehmoment.setWidthFull();
        preis.setWidthFull();

        HorizontalLayout line1 = new HorizontalLayout(bezeichnung);
        line1.setWidthFull();

        hubraum.setMin(0);

        leistung.setMin(0);
        drehmoment.setMin(0);
        HorizontalLayout line2 = new HorizontalLayout(hubraum, leistung, drehmoment);
        line2.setWidthFull();

        preis.setMin(0);
        preis.setStepButtonsVisible(true);
        preis.setStep(1);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        binder.bindInstanceFields(this);
        binder.setBean(new Motorcycle());

        add(line1, line2, preis, new HorizontalLayout(save, cancel));
    }


//    Actions------------------------------------------



    private void onSave() {

        motoService.save(binder.getBean());
        System.out.println(binder.getBean().getBezeichnung());
        Notification.show("Neues Model erfolgreich angelegt!");
        binder.setBean(newMotorcycle);

    }

    private void onCancel() {
        binder.refreshFields();
        Notification.show("Abgebrochen!");
    }




}
