package com.energytop.energytop_backend.renewableEnergy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.energytop.energytop_backend.renewableEnergy.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long>{
  
}
