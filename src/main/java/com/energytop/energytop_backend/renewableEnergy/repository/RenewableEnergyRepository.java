package com.energytop.energytop_backend.renewableEnergy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;

public interface RenewableEnergyRepository extends JpaRepository<RenewableEnergies, Long> {

  List<RenewableEnergies> findByEnergyType_EnergyNameAndYear(String energyName, int year);

  List<RenewableEnergies> findByCountryId(Long countryId);

  List<RenewableEnergies> findByEnergyTypeId(Long energyTypeId);

  Optional<RenewableEnergies> findFirstByEnergyTypeId(Long energyTypeId);

  Optional<RenewableEnergies> findFirstByEnergyTypeIdAndCountryIdAndYear(Long energyTypeId, Long countryId, int year);

  @Query("SELECT re FROM RenewableEnergies re WHERE re.energyType.id = :energyTypeId AND re.country.id = :countryId AND re.year = :year AND re.id != :currentId")
  Optional<RenewableEnergies> findSecondByEnergyTypeIdAndCountryIdAndYearExcludingCurrent(Long energyTypeId, Long countryId, int year, Long currentId);

}
