package com.energytop.energytop_backend.countries.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.energytop.energytop_backend.countries.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long>{
  
}
