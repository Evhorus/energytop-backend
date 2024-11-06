package com.energytop.energytop_backend.countries.services;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.countries.dto.CreateCountryDto;
import com.energytop.energytop_backend.countries.dto.UpdateCountryDto;
import com.energytop.energytop_backend.countries.entities.Country;

public interface CountriesService {
  PaginatedResponseDto<Country> findAll(Pageable pageable);

  Optional<Country> findById(Long id);

  Country create(CreateCountryDto createCountryDto);

  String update(Long id, UpdateCountryDto updateCountryDto);

  void remove(Long id);  
}
