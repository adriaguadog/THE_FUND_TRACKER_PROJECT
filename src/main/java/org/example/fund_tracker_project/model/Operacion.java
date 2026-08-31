package org.example.fund_tracker_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Operacion {

    private long idOperacion;
    private TipoOperacion tipoOperacion;
    private double importe;
    private double rentabilidad;
    private LocalDate fecha;
    private double participaciones;
    private double precioUnitario;
    private long idUsuario;
    private long idActivo;
    private EstadoOperacion estadoOperacion;

    public Operacion(TipoOperacion tipoOperacion, double importe, double rentabilidad, LocalDate fecha, double participaciones, double precioUnitario, long idUsuario, long idActivo) {
        this.tipoOperacion = tipoOperacion;
        this.importe = importe;
        this.rentabilidad = rentabilidad;
        this.fecha = fecha;
        this.participaciones = participaciones;
        this.precioUnitario = precioUnitario;
        this.idUsuario = idUsuario;
        this.idActivo = idActivo;
        estadoOperacion=EstadoOperacion.EN_PROCESO;
    }
}
