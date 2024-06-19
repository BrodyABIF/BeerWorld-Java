package at.spengergasse.vaadin.services;

import at.spengergasse.vaadin.domain.Motorcycle;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;


public interface MotorcycleRepository
        extends CrudRepository<Motorcycle, Long>,
            ListPagingAndSortingRepository<Motorcycle, Long> {


    @Query("""
        select *
        from MOTORCYCLE moto
        where moto.SHOWID is null 
""")
    public List<Motorcycle> findFreeMoto();

}
