package com.energytop.energytop_backend.renewableEnergy.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;
import com.energytop.energytop_backend.renewableEnergy.dto.CountryEnergyTotalDto;
import com.energytop.energytop_backend.renewableEnergy.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.renewableEnergy.dto.RenewableEnergyPercentageDto;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;
import com.energytop.energytop_backend.renewableEnergy.service.RenewableEnergyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/renewable-energy")
public class RenewableEnergyController {

  @Autowired
  public RenewableEnergyService renewableEnergyService;

  @GetMapping
  public PaginatedResponseDto<RenewableEnergies> findAll(@PageableDefault(size = 10) Pageable pageable) {
    return renewableEnergyService.findAll(pageable);
  }

  @GetMapping("/total-production")
  public List<CountryEnergyTotalDto> getTotalRenewableEnergyBySourceAndCountry(
      @RequestParam String energyName,
      @RequestParam int year) {
    return renewableEnergyService.getTotalRenewableEnergyBySourceAndCountry(energyName, year);
  }

  @GetMapping("/renewable-percentage")
  public List<RenewableEnergyPercentageDto> getRenewableEnergyPercentageByRegion() {
    return renewableEnergyService.calculateRenewableEnergyPercentageByRegion();
  }

  @GetMapping("/energy-types")
  public List<EnergyType> getEnergyTypes() {
    return renewableEnergyService.getEnergyTypes();
  }

  @GetMapping("/countries")
  public List<Country> getCountries() {
    return renewableEnergyService.getCountries();
  }

  @PostMapping("/seed/energy-types")
  public List<EnergyType> saveEnergyTypes(@RequestBody List<EnergyType> energyTypes) {
    return renewableEnergyService.saveEnergyTypes(energyTypes);
  }

  @PostMapping("/seed/countries")
  public List<Country> saveCountries(@RequestBody List<Country> countries) {
    return renewableEnergyService.saveCountries(countries);
  }

  @PostMapping("/seed/renewable-energies")
  public List<RenewableEnergies> saveRenewableEnergies(@RequestBody List<RenewableEnergies> countries) {
    return renewableEnergyService.saveRenewableEnergies(countries);
  }

}
