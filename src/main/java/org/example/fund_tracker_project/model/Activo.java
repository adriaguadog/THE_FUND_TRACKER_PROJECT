package org.example.fund_tracker_project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Activo {

    @JsonProperty("name")
    private String nombre;

    private TipoActivo tipoActivo;

    private String gestora;

    @JsonProperty("isin")
    private String isin;

    private long idActivo;

    @JsonProperty("symbol")
    private String ticker;

    public Activo(String ticker) {
        this.ticker = ticker;
    }

    public Activo(String nombre, TipoActivo tipoActivo, String gestora, String isin) {
        this.nombre = nombre;
        this.tipoActivo = tipoActivo;
        this.gestora = gestora;
        this.isin = isin;


    }
}
