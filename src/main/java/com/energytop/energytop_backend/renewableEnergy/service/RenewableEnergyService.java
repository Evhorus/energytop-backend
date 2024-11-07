package com.energytop.energytop_backend.renewableEnergy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.common.dto.SearchDto;
import com.energytop.energytop_backend.renewableEnergy.dto.CreateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.dto.UpdateRenewableEnergyDto;
import com.energytop.energytop_backend.renewableEnergy.entities.RenewableEnergies;

public interface RenewableEnergyService {

  PaginatedResponseDto<RenewableEnergies> findAll(Pageable pageable);

  Optional<RenewableEnergies> findById(Long id);

  RenewableEnergies create(CreateRenewableEnergyDto createRenewableEnergyDto);

  String update(Long id, UpdateRenewableEnergyDto updateRenewableEnergyDto);

  List<RenewableEnergies> searchRenewableEnergies(SearchDto searchDto);

  void remove(Long id);
}
