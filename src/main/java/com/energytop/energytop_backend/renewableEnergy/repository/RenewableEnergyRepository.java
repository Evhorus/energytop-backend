package com.energytop.energytop_backend.renewableEnergy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergy;

public interface RenewableEnergyRepository extends JpaRepository<RenewableEnergy, Long> {

  List<RenewableEnergy> findByEnergyType_EnergyNameAndYear(String energyName, int year);

}
