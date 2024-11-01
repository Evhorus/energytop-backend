package com.energytop.energytop_backend.renewableEnergy.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public List<RenewableEnergies> findAll() {
    return renewableEnergiesRepository.findAll();
  }

  /*
   * Obtener la producción total de energía renovable por tipo de fuente en un
   * año específico, agrupada por regiones
   */

  private Double getTotalRenewableEnergyBySourceAndCountry(String source, String country) {

    // Traer la base de datos List = array toda la data...

    // Filtar por año y por source if(List.source == source && List.country ==
    // country) -> List ,

    // Lista con filtro ya aplicado

    // List.production + sumar todo y retorna el resultado,

    return 12.22;
  }

  /*
   * Calcular el porcentaje de energía renovable en el consumo eléctrico total de
   * cada región.
   */

  /*
   * Obtener la tendencia de la capacidad instalada de energía solar a lo largo de
   * los años.
   */

  /*
   * Obtener los 10 países con mayor producción de energía eólica en un año
   * específico.
   */

  /*
   * Listar todas las fuentes de energía y su participación en el consumo
   * eléctrico
   * total a nivel global
   */

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
