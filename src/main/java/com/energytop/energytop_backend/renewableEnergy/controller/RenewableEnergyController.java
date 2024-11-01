package com.energytop.energytop_backend.renewableEnergy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.renewableEnergy.entities.Country;
import com.energytop.energytop_backend.renewableEnergy.entities.EnergyType;
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
  public List<RenewableEnergies> findAll() {
    return renewableEnergyService.findAll();
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
