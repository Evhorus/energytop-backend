package com.energytop.energytop_backend.renewableEnergy.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.energytop.energytop_backend.renewableEnergy.dto.CountryEnergyTotalDto;
import com.energytop.energytop_backend.renewableEnergy.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.renewableEnergy.dto.RenewableEnergyPercentageDto;
import com.energytop.energytop_backend.renewableEnergy.entities.Country;
import com.energytop.energytop_backend.renewableEnergy.entities.EnergyType;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;
import com.energytop.energytop_backend.renewableEnergy.repository.CountryRepository;
import com.energytop.energytop_backend.renewableEnergy.repository.EnergyTypeRepository;
import com.energytop.energytop_backend.renewableEnergy.repository.RenewableEnergiesRepository;

@Service
public class RenewableEnergyService {

  @Autowired
  private RenewableEnergiesRepository renewableEnergiesRepository;
  @Autowired
  private EnergyTypeRepository energyTypeRepository;
  @Autowired
  private CountryRepository countryRepository;

  /*
   * Listar todas las fuentes de energía y su participación en el consumo
   * eléctrico
   * total a nivel global
   */

  @Transactional
  public PaginatedResponseDto<RenewableEnergies> findAll(Pageable pageable) {
    Page<RenewableEnergies> page = renewableEnergiesRepository.findAll(pageable);
    return new PaginatedResponseDto<>(
        page.getContent(),
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

  /*
   * Obtener la producción total de energía renovable por tipo de fuente en un
   * año específico, agrupada por regiones
   */

  @Transactional
  public List<CountryEnergyTotalDto> getTotalRenewableEnergyBySourceAndCountry(String energyName, int year) {

    List<RenewableEnergies> renewableEnergies = renewableEnergiesRepository
        .findByEnergyType_EnergyNameAndYear(energyName, year);

    return renewableEnergies.stream()
        .collect(
            Collectors.groupingBy(
                energy -> energy.getCountry().getCountryName(),
                Collectors.summingDouble(RenewableEnergies::getProduction)))
        .entrySet().stream()
        .map(entry -> new CountryEnergyTotalDto(entry.getKey(), entry.getValue(), energyName, year)) 
        .collect(Collectors.toList());
  }
  /*
   * Calcular el porcentaje de energía renovable en el consumo eléctrico total de
   * cada pais.
   */

  @Transactional
  public List<RenewableEnergyPercentageDto> calculateRenewableEnergyPercentageByRegion() {

    List<RenewableEnergies> renewableEnergies = renewableEnergiesRepository.findAll();

    Map<String, Double> renewableProductionByCountry = renewableEnergies.stream()
        .collect(Collectors.groupingBy(energy -> energy.getCountry().getCountryName(),
            Collectors.summingDouble(RenewableEnergies::getProduction)));

    Map<String, Double> totalConsumptionByCountry = renewableEnergies.stream()
        .collect(Collectors.groupingBy(energy -> energy.getCountry().getCountryName(),
            Collectors.summingDouble(RenewableEnergies::getConsumption)));

    // Calcular el porcentaje de energía renovable por país
    return totalConsumptionByCountry.keySet().stream()
        .map(country -> {
          double totalConsumption = totalConsumptionByCountry.get(country);
          double renewableProduction = renewableProductionByCountry.getOrDefault(country, 0.0);
          double percentage = (totalConsumption > 0) ? (renewableProduction / totalConsumption) * 100 : 0;
          return new RenewableEnergyPercentageDto(country, percentage);
        }).collect(Collectors.toList());

  }

  /*
   * Obtener la tendencia de la capacidad instalada de segun tipo de energia a lo
   * largo de
   * los años.
   */

  /*
   * Obtener los 10 países con mayor producción de energía eólica en un año
   * específico.
   */

  @Transactional
  public List<EnergyType> getEnergyTypes() {
    return energyTypeRepository.findAll();
  }

  @Transactional
  public List<Country> getCountries() {
    return countryRepository.findAll();
  }

  // SEEDS llenar base datos
  @Transactional
  public List<EnergyType> saveEnergyTypes(List<EnergyType> energyTypes) {
    return energyTypeRepository.saveAll(energyTypes);
  }

  @Transactional
  public List<Country> saveCountries(List<Country> countries) {
    return countryRepository.saveAll(countries);
  }

  @Transactional
  public List<RenewableEnergies> saveRenewableEnergies(List<RenewableEnergies> renewableEnergiesList) {
    for (RenewableEnergies renewableEnergy : renewableEnergiesList) {
      if (renewableEnergy.getEnergyType() != null && renewableEnergy.getEnergyType().getId() != null) {
        Long energyTypeId = renewableEnergy.getEnergyType().getId();
        EnergyType energyType = energyTypeRepository.findById(energyTypeId)
            .orElseThrow(() -> new RuntimeException("EnergyType not found"));
        renewableEnergy.setEnergyType(energyType);
      }

      if (renewableEnergy.getCountry() != null && renewableEnergy.getCountry().getId() != null) {
        Long countryId = renewableEnergy.getCountry().getId();
        Country country = countryRepository.findById(countryId)
            .orElseThrow(() -> new RuntimeException("Country not found"));
        renewableEnergy.setCountry(country);
      }
    }

    return renewableEnergiesRepository.saveAll(renewableEnergiesList);
  }

}
