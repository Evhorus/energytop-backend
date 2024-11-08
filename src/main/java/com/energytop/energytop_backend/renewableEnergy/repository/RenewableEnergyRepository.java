package com.energytop.energytop_backend.renewableEnergy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;

public interface RenewableEnergyRepository extends JpaRepository<RenewableEnergies, Long> {

  List<RenewableEnergies> findByEnergyType_EnergyNameAndYear(String energyName, int year);

  List<RenewableEnergies> findByCountryId(Long countryId);

  List<RenewableEnergies> findByEnergyTypeId(Long energyTypeId);

  Optional<RenewableEnergies> findFirstByEnergyTypeId(Long energyTypeId);

}
