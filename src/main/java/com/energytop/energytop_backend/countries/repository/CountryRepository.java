package com.energytop.energytop_backend.countries.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.energytop.energytop_backend.countries.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
        // Búsqueda por countryName o countryCode
        @Query("SELECT c FROM Country c WHERE " +
                        "(:searchBy = 'countryName' AND c.countryName LIKE %:searchTerm%) OR " +
                        "(:searchBy = 'countryCode' AND c.countryCode LIKE %:searchTerm%)")
        List<Country> searchCountries(String searchTerm, String searchBy);

}
