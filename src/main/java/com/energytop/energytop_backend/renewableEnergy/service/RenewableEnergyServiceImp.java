package com.energytop.energytop_backend.renewableEnergy.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.countries.repository.CountryRepository;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;
import com.energytop.energytop_backend.energyTypes.repository.EnergyTypeRepository;
import com.energytop.energytop_backend.renewableEnergy.dto.CountryEnergyTotalDto;
import com.energytop.energytop_backend.renewableEnergy.dto.CreateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.dto.RenewableEnergyPercentageDto;
import com.energytop.energytop_backend.renewableEnergy.dto.UpdateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergy;
import com.energytop.energytop_backend.renewableEnergy.repository.RenewableEnergyRepository;


@Service
public class RenewableEnergyServiceImp implements RenewableEnergyService {

  @Autowired
  private RenewableEnergyRepository renewableEnergiesRepository;
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
  public PaginatedResponseDto<RenewableEnergy> findAll(Pageable pageable) {
    Page<RenewableEnergy> page = renewableEnergiesRepository.findAll(pageable);
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

    List<RenewableEnergy> renewableEnergies = renewableEnergiesRepository
        .findByEnergyType_EnergyNameAndYear(energyName, year);

    return renewableEnergies.stream()
        .collect(
            Collectors.groupingBy(
                energy -> energy.getCountry().getCountryName(),
                Collectors.summingDouble(RenewableEnergy::getProduction)))
        .entrySet().stream()
        .map(entry -> new CountryEnergyTotalDto(entry.getKey(), entry.getValue(), energyName, year))
        .collect(Collectors.toList());
  }
  /*
   * Calcular el porcentaje de energía renovable en el consumo eléctrico total de
   * cada pais.
   */

  /* total de produccion año * */

  /* Productioncolombia = 2energias produccion += 25 = */

  /* consumo / produccion * 100 */

  @Transactional
  public List<RenewableEnergyPercentageDto> calculateRenewableEnergyPercentageByRegion() {

    List<RenewableEnergy> renewableEnergies = renewableEnergiesRepository.findAll();

    Map<String, Double> renewableProductionByCountry = renewableEnergies.stream()
        .collect(Collectors.groupingBy(energy -> energy.getCountry().getCountryName(),
            Collectors.summingDouble(RenewableEnergy::getProduction)));

    Map<String, Double> totalConsumptionByCountry = renewableEnergies.stream()
        .collect(Collectors.groupingBy(energy -> energy.getCountry().getCountryName(),
            Collectors.summingDouble(RenewableEnergy::getConsumption)));

    // Calcular el porcentaje de energía renovable por país
    return totalConsumptionByCountry.keySet().stream()
        .map(country -> {
          double totalConsumption = totalConsumptionByCountry.get(country);
          double renewableProduction = renewableProductionByCountry.getOrDefault(country, 0.0);
          double percentage = (totalConsumption > 0) ? (totalConsumption / renewableProduction) * 100 : 0;
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

  /// CRUD!!!!!!!!!!!!!!!!!!!!!!!

  @Override
  @Transactional
  public Optional<RenewableEnergy> findById(Long id) {
    return renewableEnergiesRepository.findById(id);
  }

  @Override
  @Transactional
  public RenewableEnergy create(CreateRenewableEnergyDto createRenewableEnergyDto) {
    // Validación y asignación del tipo de energía
    EnergyType energyType = null;
    if (createRenewableEnergyDto.getEnergyType() != null) {
      Long energyTypeId = createRenewableEnergyDto.getEnergyType();
      energyType = energyTypeRepository.findById(energyTypeId)
          .orElseThrow(() -> new RuntimeException("EnergyType not found"));
    }

    // Validación y asignación del país
    Country country = null;
    if (createRenewableEnergyDto.getCountry() != null) {
      Long countryId = createRenewableEnergyDto.getCountry();
      country = countryRepository.findById(countryId)
          .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    // Crear un nuevo objeto RenewableEnergy y asignar valores del DTO
    RenewableEnergy renewableEnergy = new RenewableEnergy();
    renewableEnergy.setConsumption(createRenewableEnergyDto.getConsumption());
    renewableEnergy.setProduction(createRenewableEnergyDto.getProduction());
    renewableEnergy.setYear(createRenewableEnergyDto.getYear());
    renewableEnergy.setEnergyType(energyType);
    renewableEnergy.setCountry(country);
    return renewableEnergiesRepository.save(renewableEnergy);
  }

  @Override
  @Transactional
  public String update(Long id, UpdateRenewableEnergyDto updateRenewableEnergyDto) {
    // Buscar la entidad RenewableEnergy existente por su ID

    System.out.println(updateRenewableEnergyDto.getEnergyType());
    RenewableEnergy renewableEnergy = renewableEnergiesRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("RenewableEnergy not found"));

    // Validación y asignación del tipo de energía (solo si se pasa un nuevo valor
    // en el DTO)
    if (updateRenewableEnergyDto.getEnergyType() != null) {
      Long energyTypeId = updateRenewableEnergyDto.getEnergyType();
      EnergyType energyType = energyTypeRepository.findById(energyTypeId)
          .orElseThrow(() -> new RuntimeException("EnergyType not found"));
      renewableEnergy.setEnergyType(energyType);
    }

    // Validación y asignación del país (solo si se pasa un nuevo valor en el DTO)
    if (updateRenewableEnergyDto.getCountry() != null) {
      Long countryId = updateRenewableEnergyDto.getCountry();
      Country country = countryRepository.findById(countryId)
          .orElseThrow(() -> new RuntimeException("Country not found"));
      renewableEnergy.setCountry(country);
    }

    // Asignación de los valores de consumo, producción y año (si están presentes en
    // el DTO)
    if (updateRenewableEnergyDto.getConsumption() != null) {
      renewableEnergy.setConsumption(updateRenewableEnergyDto.getConsumption());
    }
    if (updateRenewableEnergyDto.getProduction() != null) {
      renewableEnergy.setProduction(updateRenewableEnergyDto.getProduction());
    }
    if (updateRenewableEnergyDto.getYear() != null) {
      renewableEnergy.setYear(updateRenewableEnergyDto.getYear());
    }
    renewableEnergiesRepository.save(renewableEnergy);
    // Guardar y retornar la entidad actualizada
    return "Energia Renovable actualizada correctamente";
  }

  @Override
  @Transactional
  public void remove(Long id) {
    RenewableEnergy renewableEnergy = renewableEnergiesRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("RenewableEnergy not found"));
    renewableEnergiesRepository.delete(renewableEnergy);
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
  public List<RenewableEnergy> saveRenewableEnergies(List<RenewableEnergy> renewableEnergiesList) {
    for (RenewableEnergy renewableEnergy : renewableEnergiesList) {
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
