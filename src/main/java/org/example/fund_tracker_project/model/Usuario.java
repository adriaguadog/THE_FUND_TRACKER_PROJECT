package org.example.fund_tracker_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String contrasenha;
    private long idUsuario;

    public Usuario(String nombre, String apellidos, String dni, String email, String contrasenha) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.contrasenha = contrasenha;
    }
}
