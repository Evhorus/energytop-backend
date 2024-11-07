package com.energytop.energytop_backend.countries.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountrySearchDTO {

  private String searchTerm;
  private String searchBy;

}
