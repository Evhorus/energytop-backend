package com.energytop.energytop_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginDto {
  @NotBlank(message = "El email no puede estar vacío.")
  @Email(message = "El formato del email no es válido.")
  private String email;

  @NotBlank(message = "La contraseña no puede estar vacía.")
  private String password;

}
