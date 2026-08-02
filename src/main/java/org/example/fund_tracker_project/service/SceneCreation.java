package org.example.fund_tracker_project.service;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.fund_tracker_project.HelloApplication;
import org.example.fund_tracker_project.controller.UsuarioController;
import org.example.fund_tracker_project.model.Usuario;

import java.io.IOException;

public class SceneCreation {

    public static void crearEscena(String rutaVista, Node nodoActual, String titulo) throws IOException { //el nodo es cualquier elemento de la vista actual
            FXMLLoader fxmlLoader= new FXMLLoader(HelloApplication.class.getResource(rutaVista));
            Scene scene=new Scene(fxmlLoader.load());
            Stage stage=(Stage)(nodoActual.getScene().getWindow()); //cojo la ventana actual
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        }

    public static void crearEscenaUsuario(Node nodoActual, String rutaVista, Usuario usuario) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(rutaVista));
        Parent root = fxmlLoader.load();
        UsuarioController controller = fxmlLoader.getController();
        controller.setUsuario(usuario);

        Stage stage = (Stage) nodoActual.getScene().getWindow();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("Usuario");
        stage.setScene(new Scene(root, bounds.getWidth(), bounds.getHeight()));
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();
    }

    }

