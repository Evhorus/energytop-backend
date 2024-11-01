package com.energytop.energytop_backend.renewableEnergy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.energytop.energytop_backend.renewableEnergy.entities.EnergyType;

@Repository
public interface EnergyTypeRepository extends JpaRepository<EnergyType, Long> {

}
