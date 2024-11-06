package com.energytop.energytop_backend.countries.services;

import java.util.List;
import java.util.Optional;
import com.energytop.energytop_backend.countries.dto.CreateCountryDto;
import com.energytop.energytop_backend.countries.dto.UpdateCountryDto;
import com.energytop.energytop_backend.countries.entities.Country;

public interface CountriesService {
  List<Country> findAll();

  Optional<Country> findById(Long id);

  Country create(CreateCountryDto createCountryDto);

  String update(Long id, UpdateCountryDto updateCountryDto);

  void remove(Long id);  
}
