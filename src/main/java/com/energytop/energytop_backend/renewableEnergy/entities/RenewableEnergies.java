package com.energytop.energytop_backend.renewableEnergy.entities;

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
@Table(name = "renewable_energies")
public class RenewableEnergies {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "consumption")
  private Double consumption; //Consumo 

  @Column(name = "production")
  private Double production; // Produccion 

  @Column(name = "year")
  private Integer year;

  @ManyToOne
  @JoinColumn(name = "energy_type_id")
  private EnergyType energyType;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private Country country;
}