package com.energytop.energytop_backend.energyTypes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.energytop.energytop_backend.energyTypes.entities.EnergyType;

@Repository
public interface EnergyTypeRepository extends JpaRepository<EnergyType, Long> {

  @Query(value = "SELECT * FROM energy_types e WHERE LOWER(unaccent(e.energy_name)) LIKE LOWER(concat(:searchTerm, '%'))", nativeQuery = true)
  List<EnergyType> findByEnergyNameStartingWithIgnoreCase(@Param("searchTerm") String searchTerm);

}
