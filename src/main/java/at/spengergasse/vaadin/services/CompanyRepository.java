package at.spengergasse.vaadin.services;

import at.spengergasse.vaadin.domain.Company;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CompanyRepository extends
        CrudRepository<Company, Long>,
        ListPagingAndSortingRepository<Company, Long> {

    @Modifying
    @Query("""
        UPDATE BEER beer
        SET beer.show_id = :fkCompany 
        WHERE beer.ID = :beerID
    """)
    public void hire(
            @Param("fkCompany") Long fkCompany,
            @Param("beerID") Long beerID);

    @Modifying
    @Query("""
        UPDATE BEER beer
        SET beer.SHOW_ID = null
        WHERE beer.ID = :beerID
    """)
    public void delete(
            @Param("beerID") Long beerID);

    @Query("""
        select *
        from COMPANY company
        where company.ID = :id
    """)
    public Company findByID(@Param("id") Long id);

    List<Company> findAllById(Long id);

}
