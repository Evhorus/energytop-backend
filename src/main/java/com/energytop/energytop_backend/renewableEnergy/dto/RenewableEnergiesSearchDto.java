package com.energytop.energytop_backend.renewableEnergy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenewableEnergiesSearchDto {
  private String searchTerm;
  private String searchBy;

}
