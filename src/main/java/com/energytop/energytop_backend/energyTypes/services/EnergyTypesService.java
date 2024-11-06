package com.energytop.energytop_backend.energyTypes.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.energyTypes.dto.CreateEnergyTypeDto;
import com.energytop.energytop_backend.energyTypes.dto.UpdateEnergyTypeDto;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;

public interface EnergyTypesService {

  PaginatedResponseDto<EnergyType> findAll( Pageable pageable);

  Optional<EnergyType> findById(Long id);

  EnergyType create(CreateEnergyTypeDto createEnergyTypeDto);

  String update(Long id, UpdateEnergyTypeDto updateEnergyTypeDto);

  void remove(Long id);

}
