package com.energytop.energytop_backend.countries.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.common.dto.SearchDto;
import com.energytop.energytop_backend.common.helpers.StringUtils;
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
    // Verificar si ya existe un país con el mismo nombre (o código, dependiendo de
    // tu lógica)
    Optional<Country> existingCountry = countryRepository.findByCountryName(createCountryDto.getCountryName());
    if (existingCountry.isPresent()) {
      throw new IllegalArgumentException("Ya existe un país con ese nombre");
    }

    // Crear una nueva entidad Country
    Country country = new Country();
    country.setCountryName(createCountryDto.getCountryName().trim());
    country.setCountryCode(createCountryDto.getCountryCode().trim());
    country.setPopulation(createCountryDto.getPopulation());

    // Guardar la nueva entidad en la base de datos
    Country savedCountry = countryRepository.save(country);

    // Retornar el país guardado (si deseas devolver un DTO en lugar de la entidad,
    // puedes mapearlo aquí)
    return savedCountry;
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

  @Override
  @Transactional
  public List<Country> searchCountries(SearchDto searchDTO) {
    String searchTerm = StringUtils.removeAccents(searchDTO.getSearchTerm()).toLowerCase();
    String searchBy = searchDTO.getSearchBy();

    if ("countryName".equals(searchBy)) {
      return countryRepository.searchByCountryName(searchTerm);
    } else if ("countryCode".equals(searchBy)) {
      return countryRepository.findByCountryCodeStartingWithIgnoreCase(searchTerm);
    } else {
      throw new IllegalArgumentException("El campo de búsqueda '" + searchBy + "' no es válido.");
    }
  }

}
