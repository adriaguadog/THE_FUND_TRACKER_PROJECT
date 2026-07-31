package org.example.fund_tracker_project.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.fund_tracker_project.HelloApplication;

import java.io.IOException;

public class SceneCreation {

    public static void crearEscena(String rutaVista, Node nodoActual) throws IOException { //el nodo es cualquier elemento de la vista actual
            FXMLLoader fxmlLoader= new FXMLLoader(HelloApplication.class.getResource(rutaVista));
            Scene scene=new Scene(fxmlLoader.load());
            Stage stage=(Stage)(nodoActual.getScene().getWindow()); //cojo la ventana actual
            stage.setScene(scene);
            stage.show();
        }

        //TODO CONTINUAR EL METODO Y CREAR UNO PASANDO EL USUARIO DE UNA PANTALLA A OTRA


    }

