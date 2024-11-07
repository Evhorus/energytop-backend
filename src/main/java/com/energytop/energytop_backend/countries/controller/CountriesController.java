package com.energytop.energytop_backend.countries.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.countries.dto.CountrySearchDTO;
import com.energytop.energytop_backend.countries.dto.CreateCountryDto;
import com.energytop.energytop_backend.countries.dto.UpdateCountryDto;
import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.countries.services.CountriesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/countries")
public class CountriesController {

  @Autowired
  private CountriesService countriesService;

  @GetMapping
  public PaginatedResponseDto<Country> getAllCountries(@PageableDefault(size = 10) Pageable pageable) {
    return countriesService.findAll(pageable);

  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Country>> getCountryById(@PathVariable Long id) {
    Optional<Country> country = countriesService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(country);
  }

  @PostMapping
  public ResponseEntity<String> createCountry(@RequestBody @Valid CreateCountryDto createCountryDto) {
    countriesService.create(createCountryDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("País creado correctamente");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> updateCountry(@PathVariable Long id,
      @RequestBody @Valid UpdateCountryDto updateCountryDto) {
    countriesService.update(id, updateCountryDto);
    return ResponseEntity.status(HttpStatus.OK).body("País actualizado correctamente");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCountry(@PathVariable Long id) {
    countriesService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).body("País eliminado correctamente");
  }

  @GetMapping("/search")
  public List<Country> searchCountries(@RequestParam String searchTerm, @RequestParam String searchBy) {
    CountrySearchDTO searchDTO = new CountrySearchDTO();
    searchDTO.setSearchTerm(searchTerm);
    searchDTO.setSearchBy(searchBy);
    return countriesService.searchCountries(searchDTO);
  }

}
