package at.spengergasse.vaadin.services;

import at.spengergasse.vaadin.domain.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends
        CrudRepository<Company, Long>,
        ListPagingAndSortingRepository<Company, Long> {
}
