package org.example.fund_tracker_project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)

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

    @JsonProperty("exchange")
    private String exchange;

    @JsonProperty("mic_code")
    private String micCode;

    private String tickerYahoo;

    public Activo(String ticker) {
        this.ticker = ticker;
    }

    public Activo(String nombre, TipoActivo tipoActivo, String gestora, String isin) {
        this.nombre = nombre;
        this.tipoActivo = tipoActivo;
        this.gestora = gestora;
        this.isin = isin;
    }

    @Override
    public String toString() {
        String t = ticker != null ? ticker : "";
        String n = nombre != null ? nombre : "";
        String i = isin != null ? isin : "";

        int tickerWidth = 10;
        int nameWidth = 40;

        if (n.length() > nameWidth) {
            n = n.substring(0, nameWidth - 1) + "…";
        }

        String formatted = String.format("%-" + tickerWidth + "s  %-" + nameWidth + "s  %s", t, n, i).trim();
        return formatted;
    }
    }

