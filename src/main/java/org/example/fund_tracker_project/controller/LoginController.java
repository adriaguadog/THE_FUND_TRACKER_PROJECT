package org.example.fund_tracker_project.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.fund_tracker_project.dao.UsuarioDao;
import org.example.fund_tracker_project.model.Usuario;
import org.example.fund_tracker_project.service.AlertCreation;
import org.example.fund_tracker_project.service.SceneCreation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

        @FXML
        private Button btnLogin;

        @FXML
        private Label lblMensaje;

        @FXML
        private Hyperlink linkRegistro;

        @FXML
        private TextField txtEmail;

        @FXML
        private PasswordField txtPassword;

        private UsuarioDao usuarioDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        instances();
        initGUI();
        actions();
    }

    private void initGUI() {
    }

    private void instances() {
        usuarioDao=new UsuarioDao();
    }

    private void actions() {
        btnLogin.setOnAction(event -> {
                    try {
                        Usuario usuario = usuarioDao.hacerLogin(txtEmail.getText().trim(), txtPassword.getText().trim());
                        if (usuario==null){
                            AlertCreation.crearInformacion("Credenciales incorrectas", "Mail o contrasenha incorrectos");
                        }else{
                            //TODO ABRIR PANTALLA PRINCIPAL
                        }
                    } catch (SQLException e) {
                        AlertCreation.crearFallo("Error", "No se pudo conectar con la base de datos");
                    }
                }
                       );
        linkRegistro.setOnAction(event -> {
            try {
                SceneCreation.crearEscena("/org/example/fund_tracker_project/registro.fxml",btnLogin);
            } catch (IOException e) {
                AlertCreation.crearFallo("Error", "No se ha encontrado la vista");
            }
        });
    }
}

