package com.energytop.energytop_backend.renewableEnergy.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.renewableEnergy.dto.CreateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.dto.UpdateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergy;

public interface RenewableEnergyService {

  PaginatedResponseDto<RenewableEnergy> findAll(Pageable pageable);

  Optional<RenewableEnergy> findById(Long id);

  RenewableEnergy create(CreateRenewableEnergyDto createRenewableEnergyDto);

  String update(Long id,UpdateRenewableEnergyDto updateRenewableEnergyDto);

  void remove(Long id);
}
