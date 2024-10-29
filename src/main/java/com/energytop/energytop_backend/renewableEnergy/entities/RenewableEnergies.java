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

  @Column(name = "energy_type")
  private String energyType;

  // @ManyToOne
  // @JoinColumn(name = "region_id")
  // private String region;

  @Column(name = "consumption_year")
  private Double consumptionYear;

  @Column(name = "year")
  private Integer year;

}
