package org.example.fund_tracker_project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.fund_tracker_project.dao.ActivoDao;
import org.example.fund_tracker_project.model.Activo;
import org.example.fund_tracker_project.model.LineaCartera;
import org.example.fund_tracker_project.model.Usuario;
import org.example.fund_tracker_project.service.AlertCreation;
import org.example.fund_tracker_project.service.GestorAPI;
import org.example.fund_tracker_project.service.SceneCreation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

    @FXML
    private TextField txtBuscarActivo;

    @FXML
    private ListView<Activo> listViewActivos;

    private ObservableList<Activo> activosObservable;

    private FilteredList<Activo> activosFiltrados;

    private LineChart<String, Number> chartRentabilidad;
    @FXML
    private StackPane stackBuscador;

    private Usuario usuario;

    private ActivoDao activoDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        activoDao= new ActivoDao();

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
        // añado clase css
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

        // listview
        activosObservable = FXCollections.observableArrayList();
        activosFiltrados = new FilteredList<>(activosObservable, b -> true);

        try {
            activosObservable.setAll(crearCatalogo());

            // Configurar la ListView con la lista filtrada
            listViewActivos.setItems(activosFiltrados);

            // Configurar el listener de búsqueda en tiempo real
            txtBuscarActivo.textProperty().addListener((obs, oldText, newText) -> {
                String textoBusqueda = (newText == null || newText.isEmpty()) ? "" : newText.toLowerCase();

                activosFiltrados.setPredicate(activo -> {
                    if (textoBusqueda.isEmpty()) {
                        return true;
                    }

                    boolean coincideTicker = activo.getTicker() != null &&
                            activo.getTicker().toLowerCase().contains(textoBusqueda);
                    boolean coincideNombre = activo.getNombre() != null &&
                            activo.getNombre().toLowerCase().contains(textoBusqueda);
                    boolean coincideIsin = activo.getIsin() != null &&
                            activo.getIsin().toLowerCase().contains(textoBusqueda);

                    return coincideTicker || coincideNombre || coincideIsin;
                });
            });

            // Ocultar al seleccionar un elemento
            listViewActivos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    ocultarBuscador();
                    //TODO AÑADIR A LA TABLA
                }
            });

            // Ocultar al perder el foco del TextField
            txtBuscarActivo.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal && !listViewActivos.isFocused()) {
                    ocultarBuscador();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            AlertCreation.crearFallo("Error", "Error de conexión a la base de datos: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            AlertCreation.crearFallo("Error", "Error de conexión a la API: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertCreation.crearFallo("Error", "Error en el hilo: " + e.getMessage());
        }
    }

        private List<String> obtenerMesesDelAnioActual () {
            int anioActual = Year.now().getValue();
            List<String> meses = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", Locale.forLanguageTag("es"));

            for (int mes = 1; mes <= 12; mes++) {
                meses.add(formatter.format(LocalDate.of(anioActual, mes, 1)).toUpperCase(Locale.ROOT));
            }

            return meses;
        }

        private void actions () {
            linkCerrarSesion.setOnAction(event -> {
                try {
                    SceneCreation.crearEscena("/org/example/fund_tracker_project/login.fxml", linkCerrarSesion, "Login");
                } catch (IOException e) {
                    AlertCreation.crearFallo("Error", "No se ha encontrado la vista");
                }
            });

            btnAddFondo.setOnAction(event -> {
                stackBuscador.setVisible(true);
                stackBuscador.setManaged(true);

                txtBuscarActivo.requestFocus();
                txtBuscarActivo.clear();
            });
        }


    public List<Activo> crearCatalogo() throws IOException, InterruptedException, SQLException {
        if (activoDao.obtenerTodos().isEmpty()) {
            GestorAPI gestorAPI = new GestorAPI();
            List<Activo> listaActivos = gestorAPI.llamarAPI();
            for (Activo activo : listaActivos) {
                activoDao.insertarActivo(activo);
            }
            return listaActivos;
        } else {
            return activoDao.obtenerTodos();
        }
    }

        public void setUsuario (Usuario usuario){
            this.usuario = usuario;
            if (usuario != null) {
                lblSaludo.setText("Hola, " + usuario.getNombre());
            }
        }

    private void ocultarBuscador() {
        stackBuscador.setVisible(false);
        stackBuscador.setManaged(false);
        listViewActivos.getSelectionModel().clearSelection();
        txtBuscarActivo.clear();
    }


    }