package com.energytop.energytop_backend.renewableEnergy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRenewableEnergyDto {

  @NotNull(message = "El consumo no puede ser nulo")
  @DecimalMin(value = "0.000001", inclusive = false, message = "La producción debe ser mayor que 0")
  @Digits(integer = 10, fraction = 6)
  private Double consumption;

  @NotNull(message = "La producción no puede ser nula")
  @DecimalMin(value = "0.000001", inclusive = false, message = "La producción debe ser mayor que 0")
  @Digits(integer = 10, fraction = 6)
  private Double production;

  @NotNull(message = "El año no puede ser nulo")
  @Min(value = 1900, message = "El año debe ser igual o posterior a 1900")
  private Integer year;

  @NotNull(message = "El tipo de energía no puede ser nulo")
  private Long energyType; // ID representado como Long

  @NotNull(message = "El país no puede ser nulo")
  private Long country; // ID representado como Long
}
