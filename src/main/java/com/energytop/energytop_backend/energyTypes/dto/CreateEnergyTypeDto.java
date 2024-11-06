package com.energytop.energytop_backend.energyTypes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateEnergyTypeDto {

    @NotBlank(message = "El nombre del tipo de energía no puede estar vacío.")
    private String energyName;

    @NotBlank(message = "La fuente no puede estar vacía.")
    private String source;
}