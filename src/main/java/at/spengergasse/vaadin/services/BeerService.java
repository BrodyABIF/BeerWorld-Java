package at.spengergasse.vaadin.services;

import at.spengergasse.vaadin.domain.Beer;
import at.spengergasse.vaadin.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class BeerService {



    private final BeerRepository beerRepository;
    private final CompanyRepository companyRepository;


    private BeerService(@Autowired BeerRepository beerRepo, @Autowired CompanyRepository companyRepo) {

        this.beerRepository = beerRepo;
        this.companyRepository = companyRepo;
    }

    public void hire(Long fkCompany, Long beerID) {
        companyRepository.hire(fkCompany, beerID);
    }

    public List<Beer> findFreeBeer() {
        return beerRepository.findFreeBeer();
    }

    public Beer findBeerById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Beer> beer = beerRepository.findById(id);
        if (beer.isPresent()) {
            return beer.get();
        }
        return null;
    }

    public Company findCompanyById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return company.get();
        }
        return null;
    }


    public Beer saveBeer(Beer beer) {
        return beerRepository.save(beer);

    }
    public Company saveCompany(Company company) {
        return companyRepository.save(company);

    }


    public List<Beer> findBeerByCompany(Company company){
        return beerRepository.findAllByShowId(company.getId());
    }

    public List<Company> findCompanyByBeer(Beer beer){
        return companyRepository.findAllById(beer.getShowId());
    }

    public void deleteFromCompany(Beer beer){
        companyRepository.delete(beer.getId());
    }


}


