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
        // 1. Crear el loader con la ruta del FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(rutaVista));

        // 2. Cargar el FXML y crear la escena
        Scene scene=new Scene(fxmlLoader.load());

        // 3. Pedir el controller de la vista Usuario
        UsuarioController controller = fxmlLoader.getController();

        // 4. Pasar el usuario al controller
        controller.setUsuario(usuario);

        // 5. Obtener el Stage actual a partir del nodo
        Stage stage = (Stage) nodoActual.getScene().getWindow();

        // 6. Obtener los límites de la pantalla principal
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        // 7. Configurar título de la ventana
        stage.setTitle("Usuario");

        // 8. Asignar la nueva escena al stage
        stage.setScene(scene);

        // 9. Colocar la ventana en las coordenadas mínimas de la pantalla
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());

        // 10. Maximizar la ventana
        stage.setMaximized(true);

        // 11. Centrar (no es muy necesario si está maximizada, pero no molesta)
        stage.centerOnScreen();

        // 12. Mostrar la ventana
        stage.show();
    }

    }

