package org.example.fund_tracker_project.service;

import javafx.scene.control.Alert;

public class AlertCreation {

    public static void crearFallo (String titulo, String contenido){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null); //para que no salgan cabeceras raras
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public static void crearInformacion (String titulo, String contenido){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null); //para que no salgan cabeceras raras
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public static void crearWarning (String titulo, String contenido){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null); //para que no salgan cabeceras raras
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
