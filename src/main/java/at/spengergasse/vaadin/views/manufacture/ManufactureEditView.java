package at.spengergasse.vaadin.views.manufacture;

import at.spengergasse.vaadin.domain.Company;
import at.spengergasse.vaadin.services.BeerService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
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


@PageTitle(value = "Manufacture editieren")
@Route(value = "company/:" + ManufactureEditView.COMPANY_ID + "?/edit", layout = MainLayout.class)


public class ManufactureEditView extends VerticalLayout implements BeforeEnterObserver {

    public static final String COMPANY_ID = "company";

    private final BeerService service;
    private final Binder<Company> binder = new Binder<>(Company.class, false);

    private final TextField name = new TextField("Name");
    private final TextField ort = new TextField("Ort");
    private final IntegerField plz = new IntegerField("Postleitzahl");

    public final Button cancel = new Button("Cancel", LineAwesomeIcon.TIMES_SOLID.create(), e -> onCancel());
    public final Button save = new Button("Speichern", LineAwesomeIcon.SAVE.create(), e -> onSave());


    public ManufactureEditView(@Autowired BeerService service) {
        this.service = service;
        init();
    }

    private void init() {
        FormLayout formLayout = new FormLayout();
        formLayout.add();
        binder.bindInstanceFields(this);

        setWidthFull();

        name.setWidth("500px");
        ort.setWidth("350px");
        plz.setWidth ("150px");

        binder.forField(name)
                .asRequired("Name darf nicht leer sein")
                .bind(Company::getName, Company::setName);

        binder.forField(ort)
                .asRequired("Ort darf nicht leer sein")
                .bind(Company::getOrt, Company::setOrt);

        binder.forField(plz)
                .withValidator(value -> value != null && value >= 1000, "Postleitzahl muss mindestens 1000 sein")
                .bind(Company::getPlz, Company::setPlz);


        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        binder.bindInstanceFields(this);
        binder.setBean(new Company());

        VerticalLayout centeredLayout = new VerticalLayout();
        centeredLayout.setAlignItems(Alignment.CENTER);
        centeredLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeredLayout.setSizeFull();

        centeredLayout.add(name, new HorizontalLayout(ort, plz) , new HorizontalLayout(save, cancel));


        add(formLayout, centeredLayout);
    }


    private void onCancel() {
        getUI().ifPresent(ui -> ui.navigate(Manufacture.class));
    }

    private void onSave() {
        try {
            if (binder.validate().isOk()) {
                service.saveCompany(binder.getBean());
                getUI().ifPresent(ui -> ui.navigate(Manufacture.class));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setCompany(Company company) {
        if (company == null) {
            company = new Company();
        }
        binder.setBean(company);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> optional = event.getRouteParameters().getLong(COMPANY_ID);
        optional.ifPresentOrElse(aLong -> setCompany (service.findCompanyById(aLong) ), () -> setCompany(null));
    }
}
