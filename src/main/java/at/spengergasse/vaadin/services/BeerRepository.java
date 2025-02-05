package at.spengergasse.vaadin.services;

import at.spengergasse.vaadin.domain.Beer;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BeerRepository
        extends CrudRepository<Beer, Long>,
            ListPagingAndSortingRepository<Beer, Long> {



    @Query("""
        select * 
        from Beer b
        where b.show_id is null
""")
    public List<Beer> findFreeBeer();

    @Query("""
        select *
        from Beer beer
        where beer.ID = :id
    """)
    public Beer findByID(@Param("id") Long id);

    List<Beer> findAllByShowId(Long showId);
}
