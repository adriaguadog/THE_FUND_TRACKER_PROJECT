package org.example.fund_tracker_project.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.fund_tracker_project.model.LineaCartera;
import org.example.fund_tracker_project.model.Usuario;
import org.example.fund_tracker_project.service.AlertCreation;
import org.example.fund_tracker_project.service.SceneCreation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class UsuarioController implements Initializable {

    @FXML
    private Label lblSaludo;

    @FXML
    private Label lblSubtitulo;

    @FXML
    private Label lblValorTotal;

    @FXML
    private Label lblRentabilidadAcumulada;

    @FXML
    private Label lblRentabilidadYtd;

    @FXML
    private Label lblNumFondos;

    @FXML
    private Button btnAddFondo;

    @FXML
    private Hyperlink linkCerrarSesion;

    @FXML
    private TableView<LineaCartera> tblCartera;

    @FXML
    private TableColumn<?, ?> colFondo;

    @FXML
    private TableColumn<?, ?> colParticipaciones;

    @FXML
    private TableColumn<?, ?> colPrecio;

    @FXML
    private TableColumn<?, ?> colRentabilidadAcumulada;

    @FXML
    private TableColumn<?, ?> colRentabilidadYtd;

    @FXML
    private VBox chartContainer;

    private LineChart<String, Number> chartRentabilidad;

    private Usuario usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
    }

    private void initGUI() {
        tblCartera.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblCartera.setPlaceholder(new Label("Aún no tienes fondos en tu cartera"));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Mes");
        xAxis.setCategories(FXCollections.observableArrayList(obtenerMesesDelAnioActual()));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("%");
        yAxis.setAutoRanging(true);

        chartRentabilidad = new LineChart<>(xAxis, yAxis);
        chartRentabilidad.setTitle("Rentabilidad " + Year.now().getValue());
        chartRentabilidad.setAnimated(false);
        chartRentabilidad.setLegendVisible(false);
        //anhado clase css
        chartRentabilidad.getStyleClass().add("rentabilidad-chart");
        chartRentabilidad.setCreateSymbols(false);
        chartRentabilidad.setMaxWidth(Double.MAX_VALUE);
        chartRentabilidad.setMaxHeight(Double.MAX_VALUE);
        chartRentabilidad.setMinHeight(0);
        chartRentabilidad.setPrefHeight(400);
        chartRentabilidad.setData(FXCollections.observableArrayList());
        VBox.setVgrow(chartRentabilidad, Priority.ALWAYS);

        chartContainer.setSpacing(0);
        chartContainer.setFillWidth(true);
        chartContainer.setPrefHeight(420);
        chartContainer.getChildren().setAll(chartRentabilidad);
    }

    private List<String> obtenerMesesDelAnioActual() {
        int anioActual = Year.now().getValue();
        List<String> meses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", Locale.forLanguageTag("es"));

        for (int mes = 1; mes <= 12; mes++) {
            meses.add(formatter.format(LocalDate.of(anioActual, mes, 1)).toUpperCase(Locale.ROOT));
        }

        return meses;
    }

    private void actions() {
        linkCerrarSesion.setOnAction(event -> {
            try {
                SceneCreation.crearEscena("/org/example/fund_tracker_project/login.fxml", linkCerrarSesion, "Login");
            } catch (IOException e) {
                AlertCreation.crearFallo("Error", "No se ha encontrado la vista");
            }
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            lblSaludo.setText("Hola, " + usuario.getNombre());
        }
    }
}