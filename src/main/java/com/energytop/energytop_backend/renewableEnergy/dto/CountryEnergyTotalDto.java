package com.energytop.energytop_backend.renewableEnergy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryEnergyTotalDto {

  private String country;

  private Double totalProduction;

  private String energyType;

  private int year;

}
