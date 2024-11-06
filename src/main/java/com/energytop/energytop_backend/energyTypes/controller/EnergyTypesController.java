package com.energytop.energytop_backend.energyTypes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.energyTypes.dto.CreateEnergyTypeDto;
import com.energytop.energytop_backend.energyTypes.dto.UpdateEnergyTypeDto;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;
import com.energytop.energytop_backend.energyTypes.services.EnergyTypesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/energy-types")
public class EnergyTypesController {

  @Autowired
  EnergyTypesService energyTypesService;

  @GetMapping
  public PaginatedResponseDto<EnergyType> getAllEnergyTypes(@PageableDefault(size = 10) Pageable pageable) {
    return energyTypesService.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<EnergyType>> getEnergyTypeById(@PathVariable Long id) {
    Optional<EnergyType> energyType = energyTypesService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(energyType);
  }

  @PostMapping
  public ResponseEntity<String> createEnergyType(@RequestBody @Valid CreateEnergyTypeDto createEnergyTypeDto) {
    energyTypesService.create(createEnergyTypeDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("Tipo de energía creado correctamente");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> updateEnergyType(@PathVariable Long id,
      @RequestBody @Valid UpdateEnergyTypeDto updateEnergyTypeDto) {
    energyTypesService.update(id, updateEnergyTypeDto);
    return ResponseEntity.status(HttpStatus.OK).body("Tipo de energía actualizado correctamente");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteEnergyType(@PathVariable Long id) {
    energyTypesService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).body("Tipo de energía eliminado correctamente");
  }

}
