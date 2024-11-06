package com.energytop.energytop_backend.countries.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<List<Country>> getAllCountries() {
    List<Country> countries = countriesService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(countries);
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

}
