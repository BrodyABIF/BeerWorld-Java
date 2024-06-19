package at.spengergasse.vaadin.services;
import at.spengergasse.vaadin.domain.Motorcycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MotoService {



    private final MotorcycleRepository motorcycleRepository;
    private final CompanyRepository companyRepository;

    private MotoService(@Autowired MotorcycleRepository motorcycleRepository, @Autowired CompanyRepository companyRepository) {

        this.motorcycleRepository = motorcycleRepository;
        this.companyRepository = companyRepository;



    }




    public void save(Motorcycle motorcycle) {

        motorcycleRepository.save(motorcycle);

    }

    public void deleteAll(Set<Motorcycle> selectedItems) {
        motorcycleRepository.deleteAll( selectedItems );
    }


    public void hire(Long fkCompany, Long bikeID) {
        companyRepository.hire(fkCompany, bikeID);



    }

    public List<Motorcycle> findFreeMoto() {

        return motorcycleRepository.findFreeMoto();

    }


}


