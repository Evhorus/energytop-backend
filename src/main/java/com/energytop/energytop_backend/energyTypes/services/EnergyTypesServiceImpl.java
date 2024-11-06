package com.energytop.energytop_backend.energyTypes.services;

import java.util.Optional;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.energyTypes.dto.CreateEnergyTypeDto;
import com.energytop.energytop_backend.energyTypes.dto.UpdateEnergyTypeDto;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;
import com.energytop.energytop_backend.energyTypes.repository.EnergyTypeRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnergyTypesServiceImpl implements EnergyTypesService {

    @Autowired
    EnergyTypeRepository energyTypeRepository;

    @Override
    @Transactional
    public PaginatedResponseDto<EnergyType> findAll(Pageable pageable) {
        Page<EnergyType> page = energyTypeRepository.findAll(pageable);
        return new PaginatedResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty(),
                pageable,
                pageable.getSort());
    }

    @Override
    @Transactional
    public Optional<EnergyType> findById(Long id) {
        return energyTypeRepository.findById(id);
    }

    @Override
    @Transactional
    public EnergyType create(CreateEnergyTypeDto createEnergyTypeDto) {
        EnergyType energyType = new EnergyType();
        energyType.setEnergyName(createEnergyTypeDto.getEnergyName());
        energyType.setSource(createEnergyTypeDto.getSource());
        return energyTypeRepository.save(energyType);
    }

    @Override
    @Transactional
    public String update(Long id, UpdateEnergyTypeDto updateEnergyTypeDto) {

        Optional<EnergyType> energyTypeDb = energyTypeRepository.findById(id);

        if (energyTypeDb.isEmpty()) {
            throw new EntityNotFoundException("No existe un tipo de energía con el identificador: " + id);
        }

        EnergyType energyTypeToEdit = energyTypeDb.get();

        if (energyTypeToEdit.getEnergyName() != null) {
            energyTypeToEdit.setEnergyName(updateEnergyTypeDto.getEnergyName().trim());
        }
        if (energyTypeToEdit.getSource() != null) {
            energyTypeToEdit.setSource(updateEnergyTypeDto.getSource().trim());
        }

        energyTypeRepository.save(energyTypeToEdit);
        return "Tipo de energía actualizado correctamente";
    }

    @Override
    @Transactional
    public void remove(Long id) {
        if (!energyTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe un tipo de energía con el identificador: " + id);
        }

        energyTypeRepository.deleteById(id);
    }
}
