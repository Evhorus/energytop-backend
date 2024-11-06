package com.energytop.energytop_backend.countries.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CreateCountryDto {

   @NotBlank(message = "El nombre del país no puede estar vacío.")
   private String countryName;

   @NotBlank(message = "El código del país no puede estar vacío.")
   private String countryCode;

   @NotNull(message = "La población no puede ser nula")
   private Long population;

}
