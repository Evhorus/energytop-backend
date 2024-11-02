package com.energytop.energytop_backend.renewableEnergy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;

public interface RenewableEnergiesRepository extends JpaRepository<RenewableEnergies, Long> {

  List<RenewableEnergies> findByEnergyType_EnergyNameAndYear(String energyName, int year);

}
