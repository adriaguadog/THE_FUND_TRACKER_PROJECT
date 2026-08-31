package org.example.fund_tracker_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LineaCartera {

    private long idUsuario;
    private long idActivo;
    private double importe;
    private double participaciones;
    private String nombreActivo;

    public LineaCartera(long idUsuario, long idActivo, double importe, double participaciones) {
        this.idUsuario = idUsuario;
        this.idActivo = idActivo;
        this.importe = importe;
        this.participaciones = participaciones;
    }

}
