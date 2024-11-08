package com.energytop.energytop_backend.energyTypes.services;

import java.util.List;
import java.util.Optional;

import com.energytop.energytop_backend.common.dto.PaginatedResponseDto;
import com.energytop.energytop_backend.common.dto.SearchDto;
import com.energytop.energytop_backend.common.helpers.StringUtils;
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
        Optional<EnergyType> existingEnergyType = energyTypeRepository
                .findByEnergyName(createEnergyTypeDto.getEnergyName());

        if (existingEnergyType.isPresent()) {
            throw new IllegalArgumentException("Ya existe un tipo de energía con ese nombre");
        }

        EnergyType energyType = new EnergyType();
        energyType.setEnergyName(createEnergyTypeDto.getEnergyName());
        energyType.setSource(createEnergyTypeDto.getSource());

        EnergyType savedEnergyType = energyTypeRepository.save(energyType);
        return savedEnergyType;
    }

    @Override
    @Transactional
    public String update(Long id, UpdateEnergyTypeDto updateEnergyTypeDto) {
        Optional<EnergyType> energyTypeDb = energyTypeRepository.findById(id);

        if (energyTypeDb.isEmpty()) {
            throw new EntityNotFoundException("No existe un tipo de energía con el identificador: " + id);
        }

        EnergyType energyTypeToEdit = energyTypeDb.get();

        // Verificar si el nuevo nombre ya existe en la base de datos
        if (updateEnergyTypeDto.getEnergyName() != null) {
            String newEnergyName = updateEnergyTypeDto.getEnergyName().trim();

            // Validar si ya existe otro tipo de energía con el mismo nombre
            Optional<EnergyType> existingEnergyType = energyTypeRepository.findByEnergyName(newEnergyName);
            if (existingEnergyType.isPresent() && !existingEnergyType.get().getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe un tipo de energía con el nombre: " + newEnergyName);
            }

            // Si pasa la validación, actualizar el nombre
            energyTypeToEdit.setEnergyName(newEnergyName);
        }

        if (updateEnergyTypeDto.getSource() != null) {
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

    @Override
    @Transactional(readOnly = true)
    public List<EnergyType> searchEnergyTypes(SearchDto searchDto) {
        String searchTerm = StringUtils.removeAccents(searchDto.getSearchTerm()).toLowerCase();
        String searchBy = searchDto.getSearchBy();

        if ("energyName".equals(searchBy)) {
            return energyTypeRepository.searchByEnergyName(searchTerm);
        }
        throw new IllegalArgumentException("El campo de búsqueda '" + searchBy + "' no es válido.");
    }
}
