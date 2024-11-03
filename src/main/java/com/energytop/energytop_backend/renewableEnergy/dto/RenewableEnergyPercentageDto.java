package com.energytop.energytop_backend.renewableEnergy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewableEnergyPercentageDto {
  private String country; // Nombre del país
  private double percentage; // Porcentaje de energía renovable
}