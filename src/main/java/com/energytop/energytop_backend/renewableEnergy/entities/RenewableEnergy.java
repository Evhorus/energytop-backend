package com.energytop.energytop_backend.renewableEnergy.entities;

import com.energytop.energytop_backend.countries.entities.Country;
import com.energytop.energytop_backend.energyTypes.entities.EnergyType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "renewable_energy")
public class RenewableEnergy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "consumption")
  private Double consumption;

  @Column(name = "production", columnDefinition =  "DECIMAL(10, 2)")
  private Double production; 

  @Column(name = "year")
  private Integer year;

  @ManyToOne
  @JoinColumn(name = "energy_type_id")
  private EnergyType energyType;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private Country country;
}
