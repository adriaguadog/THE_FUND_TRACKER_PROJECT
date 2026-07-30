package org.example.fund_tracker_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Activo {

    private String nombre;
    private TipoActivo tipoActivo;
    private String gestora;
    private String isin;
    private long idActivo;

    public Activo(String nombre, TipoActivo tipoActivo, String gestora, String isin) {
        this.nombre = nombre;
        this.tipoActivo = tipoActivo;
        this.gestora = gestora;
        this.isin = isin;
    }
}
