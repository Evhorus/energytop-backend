package com.energytop.energytop_backend.countries.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.energytop.energytop_backend.countries.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
        Optional<Country> findByCountryName(String countryName);

        // Búsqueda por countryName o countryCode
        @Query("SELECT c FROM Country c WHERE " +
                        "(:searchBy = 'countryName' AND c.countryName LIKE %:searchTerm%) OR " +
                        "(:searchBy = 'countryCode' AND c.countryCode LIKE %:searchTerm%)")
        List<Country> searchCountries(String searchTerm, String searchBy);

        @Query(value = "SELECT * FROM countries c WHERE LOWER(unaccent(c.country_name)) LIKE LOWER(concat(:searchTerm, '%'))", nativeQuery = true)
        List<Country> findByCountryNameStartingWithIgnoreCase(@Param("searchTerm") String searchTerm);

}

// CREATE EXTENSION IF NOT EXISTS unaccent; ejecutar een la tabla de datos