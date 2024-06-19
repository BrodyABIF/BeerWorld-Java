package at.spengergasse.vaadin.services;

import at.spengergasse.vaadin.domain.Company;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface CompanyRepository extends
        CrudRepository<Company, Long>,
        ListPagingAndSortingRepository<Company, Long> {

    @Modifying
    @Query("""
        UPDATE MOTORCYCLE moto
        SET moto.SHOWID = :fkCompany 
        WHERE moto.ID = :bikeID
    """)
    public void hire(
            @Param("fkCompany") Long fkCompany,
            @Param("bikeID") Long bikeID);
}
