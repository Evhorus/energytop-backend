package com.energytop.energytop_backend.renewableEnergy.service;

import java.util.ArrayList;
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
import com.energytop.energytop_backend.common.dto.SearchDto;
import com.energytop.energytop_backend.common.helpers.StringUtils;
import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.countries.repository.CountryRepository;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;
import com.energytop.energytop_backend.energyTypes.repository.EnergyTypeRepository;
import com.energytop.energytop_backend.renewableEnergy.dto.CountryEnergyTotalDto;
import com.energytop.energytop_backend.renewableEnergy.dto.CreateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.dto.RenewableEnergyPercentageDto;
import com.energytop.energytop_backend.renewableEnergy.dto.UpdateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;
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

  @Override
  @Transactional
  public List<RenewableEnergies> searchRenewableEnergies(SearchDto searchDto) {
    String searchTerm = StringUtils.removeAccents(searchDto.getSearchTerm()).toLowerCase();
    String searchBy = searchDto.getSearchBy();

    List<RenewableEnergies> results = new ArrayList<>();

    if ("countryName".equals(searchBy)) {
      List<Country> countries = countryRepository.searchByCountryName(searchTerm);
      for (Country country : countries) {
        results.addAll(renewableEnergiesRepository.findByCountryId(country.getId()));
      }

    } else if ("energyName".equals(searchBy)) {
      List<EnergyType> energyTypes = energyTypeRepository.searchByEnergyName(searchTerm);
      for (EnergyType energyType : energyTypes) {
        results.addAll(renewableEnergiesRepository.findByEnergyTypeId(energyType.getId()));
      }

    }

    return results;
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

  /* total de produccion año * */

  /* Productioncolombia = 2energias produccion += 25 = */

  /* consumo / produccion * 100 */

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
  public Optional<RenewableEnergies> findById(Long id) {
    return renewableEnergiesRepository.findById(id);
  }

  @Override
  @Transactional
  public RenewableEnergies create(CreateRenewableEnergyDto createRenewableEnergyDto) {
    // Validación y asignación del tipo de energía
    Long energyTypeId = createRenewableEnergyDto.getEnergyType();
    EnergyType energyType = energyTypeRepository.findById(energyTypeId)
        .orElseThrow(() -> new RuntimeException("EnergyType not found"));

    // Verificar si ya existe un registro con el mismo tipo de energía, país y año
    Long countryId = createRenewableEnergyDto.getCountry();
    Optional<RenewableEnergies> existingEntry = renewableEnergiesRepository
        .findFirstByEnergyTypeIdAndCountryIdAndYear(energyTypeId, countryId, createRenewableEnergyDto.getYear());

    if (existingEntry.isPresent()) {
      throw new IllegalArgumentException("Ya existe un registro para este tipo de energía, país y año.");
    }

    // Validación y asignación del país
    Country country = null;
    if (countryId != null) {
      country = countryRepository.findById(countryId)
          .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    // Crear un nuevo objeto RenewableEnergy y asignar valores del DTO
    RenewableEnergies renewableEnergy = new RenewableEnergies();
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
    RenewableEnergies renewableEnergy = renewableEnergiesRepository.findById(id)
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

    // Si se pasa un nuevo año, verificar si ya existe un registro con el mismo tipo
    // de energía, país y año
    if (updateRenewableEnergyDto.getYear() != null) {
      int newYear = updateRenewableEnergyDto.getYear();
      Long energyTypeId = renewableEnergy.getEnergyType().getId();
      Long countryId = renewableEnergy.getCountry().getId();

      // Verificar si ya existe un registro con el mismo tipo de energía, país y año
      Optional<RenewableEnergies> existingEntry = renewableEnergiesRepository
          .findSecondByEnergyTypeIdAndCountryIdAndYearExcludingCurrent(energyTypeId, countryId, newYear,id);

      if (existingEntry.isPresent()) {
        throw new IllegalArgumentException("Ya existe un registro para este tipo de energía, país y año.");
      }

      renewableEnergy.setYear(newYear);
    }

    // Guardar y retornar la entidad actualizada
    renewableEnergiesRepository.save(renewableEnergy);
    return "Energia Renovable actualizada correctamente";
  }

  @Override
  @Transactional
  public void remove(Long id) {
    RenewableEnergies renewableEnergy = renewableEnergiesRepository.findById(id)
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
