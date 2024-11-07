package com.energytop.energytop_backend.renewableEnergy.controller;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;
import com.energytop.energytop_backend.renewableEnergy.dto.CountryEnergyTotalDto;
import com.energytop.energytop_backend.renewableEnergy.dto.CreateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.dto.RenewableEnergyPercentageDto;
import com.energytop.energytop_backend.renewableEnergy.dto.UpdateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergy;
import com.energytop.energytop_backend.renewableEnergy.service.RenewableEnergyServiceImp;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/renewable-energy")
public class RenewableEnergyController {

  @Autowired
  public RenewableEnergyServiceImp renewableEnergyService;

  @GetMapping
  public PaginatedResponseDto<RenewableEnergy> findAll(@PageableDefault(size = 10) Pageable pageable) {
    return renewableEnergyService.findAll(pageable);
  }

 @GetMapping("/{id}")
  public ResponseEntity<Optional<RenewableEnergy>> getRenewableEnergyId(@PathVariable Long id) {
    Optional<RenewableEnergy> renewableEnery = renewableEnergyService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(renewableEnery);
  }


  @PostMapping
  public ResponseEntity<String> createRenewableEnergy(
      @RequestBody @Valid CreateRenewableEnergyDto createRenewableEnergyDto) {
    renewableEnergyService.create(createRenewableEnergyDto);

    return ResponseEntity.status(HttpStatus.CREATED).body("Energia renovable creada");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> updateRenewableEnergy(@PathVariable Long id,
      @RequestBody @Valid UpdateRenewableEnergyDto updateRenewableEnergyDto) {
    renewableEnergyService.update(id, updateRenewableEnergyDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("Energia renovable actualizada correctamente");
  }

  @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRenewableEnergy(@PathVariable Long id) {
        renewableEnergyService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Energia renovable eliminada correctamente");
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

  @PostMapping("/seed/energy-types")
  public List<EnergyType> saveEnergyTypes(@RequestBody List<EnergyType> energyTypes) {
    return renewableEnergyService.saveEnergyTypes(energyTypes);
  }

  @PostMapping("/seed/countries")
  public List<Country> saveCountries(@RequestBody List<Country> countries) {
    return renewableEnergyService.saveCountries(countries);
  }

  @PostMapping("/seed/renewable-energies")
  public List<RenewableEnergy> saveRenewableEnergies(@RequestBody List<RenewableEnergy> countries) {
    return renewableEnergyService.saveRenewableEnergies(countries);
  }

}
