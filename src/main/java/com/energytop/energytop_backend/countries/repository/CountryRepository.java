package com.energytop.energytop_backend.countries.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.energytop.energytop_backend.countries.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
        Optional<Country> findByCountryName(String countryName);    

        @Query(value = "SELECT * FROM countries c WHERE LOWER(unaccent(c.country_name)) LIKE LOWER(concat(:searchTerm, '%'))", nativeQuery = true)
        List<Country> searchByCountryName(@Param("searchTerm") String searchTerm);

        public List<Country> findByCountryCodeStartingWithIgnoreCase(String countryCode);
}

// CREATE EXTENSION IF NOT EXISTS unaccent; ejecutar een la tabla de datos