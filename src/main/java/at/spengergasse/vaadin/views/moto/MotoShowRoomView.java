package at.spengergasse.vaadin.views.moto;


import at.spengergasse.vaadin.domain.Motorcycle;
import at.spengergasse.vaadin.services.MotorcycleRepository;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle(value = "Show Room")
@Route(value = "moto/showRoom", layout = MainLayout.class)
public class MotoShowRoomView extends VerticalLayout {

    private final MotorcycleRepository motoRepository;
    private final Grid<Motorcycle> showGrid = new Grid<>(Motorcycle.class, false);

    public MotoShowRoomView(@Autowired MotorcycleRepository motorcycleRepository) {
        this.motoRepository = motorcycleRepository;






    }







}
