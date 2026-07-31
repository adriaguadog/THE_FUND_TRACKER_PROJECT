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

public class RegistroController implements Initializable {

        @FXML
        private Button btnRegistro;

        @FXML
        private Label lblMensaje;

        @FXML
        private Hyperlink linkVolverLogin;

        @FXML
        private TextField txtApellidos;

        @FXML
        private TextField txtDni;

        @FXML
        private TextField txtEmail;

        @FXML
        private TextField txtNombre;

        @FXML
        private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
    }

    private void initGUI() {

    }

    private void actions() {
        linkVolverLogin.setOnAction(event ->{
            try {
                SceneCreation.crearEscena("/org/example/fund_tracker_project/login.fxml", btnRegistro, "Iniciar sesion");
            } catch (IOException e) {
                AlertCreation.crearFallo("Error", "No se ha encontrado la vista");
            }
        });

        btnRegistro.setOnAction(event -> {
            if (txtNombre.getText().isEmpty()||txtApellidos.getText().isEmpty()||txtDni.getText().isEmpty()||txtEmail.getText().isEmpty()||txtPassword.getText().isEmpty()){
                AlertCreation.crearWarning("Campos incompletos", "Debe rellenar todos los campos");
            }else {
                String nombre= txtNombre.getText().trim();
                String apellidos= txtApellidos.getText().trim();
                String email= txtEmail.getText().trim();
                String dni= txtDni.getText().trim();
                String contrasenha= txtPassword.getText().trim();

                UsuarioDao usuarioDao=new UsuarioDao();

                try {
                    if ((usuarioDao.buscarPorEmail(email)!=null)||(usuarioDao.buscarPorDni(dni)!=null)){
                        AlertCreation.crearWarning("Usuario ya registrado", "Este usuario ya tiene una cuenta");
                    }else {
                        usuarioDao.darDeAlta(new Usuario(nombre, apellidos, dni, email, contrasenha));
                        AlertCreation.crearInformacion("Registro completado", "Usuario creado correctamente");
                    }
                } catch (SQLException e) {
                    AlertCreation.crearFallo("Error", "Error de conexion a la base de datos");
                }
            }

        });
    }
}





