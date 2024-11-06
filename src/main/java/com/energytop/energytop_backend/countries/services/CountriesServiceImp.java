package com.energytop.energytop_backend.countries.services;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.countries.dto.CreateCountryDto;
import com.energytop.energytop_backend.countries.dto.UpdateCountryDto;
import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.countries.repository.CountryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CountriesServiceImp implements CountriesService {

  @Autowired
  CountryRepository countryRepository;

  @Override
  @Transactional
  public PaginatedResponseDto<Country> findAll(Pageable pageable) {
    Page<Country> page = countryRepository.findAll(pageable);
    return new PaginatedResponseDto<>(page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isFirst(),
        page.isLast(),
        page.isEmpty(),
        pageable,
        pageable.getSort());
  }

  @Override
  @Transactional
  public Optional<Country> findById(Long id) {
    return countryRepository.findById(id);
  }

  @Override
  @Transactional
  public Country create(CreateCountryDto createCountryDto) {
    Country country = new Country();
    country.setCountryName(createCountryDto.getCountryName());
    country.setCountryCode(createCountryDto.getCountryCode());
    country.setPopulation(createCountryDto.getPopulation());
    return countryRepository.save(country);
  }

  @Override
  @Transactional
  public String update(Long id, UpdateCountryDto updateCountryDto) {
    Optional<Country> countryDb = countryRepository.findById(id);

    if (countryDb.isEmpty()) {
      throw new EntityNotFoundException("No existe un país con el identificador: " + id);
    }

    Country countryToEdit = countryDb.get();

    // Actualizar nombre del país si se proporciona un nuevo valor
    if (updateCountryDto.getCountryName() != null) {
      countryToEdit.setCountryName(updateCountryDto.getCountryName().trim());
    }
    // Actualizar el código del país si se proporciona un nuevo valor
    if (updateCountryDto.getCountryCode() != null) {
      countryToEdit.setCountryCode(updateCountryDto.getCountryCode().trim());
    }
    // Actualizar la población si se proporciona un nuevo valor
    if (updateCountryDto.getPopulation() != null) {
      countryToEdit.setPopulation(updateCountryDto.getPopulation());
    }

    countryRepository.save(countryToEdit);
    return "País actualizado correctamente";
  }

  @Override
  @Transactional
  public void remove(Long id) {
    if (!countryRepository.existsById(id)) {
      throw new EntityNotFoundException("No existe un país con el identificador: " + id);
    }

    countryRepository.deleteById(id);
  }

}
