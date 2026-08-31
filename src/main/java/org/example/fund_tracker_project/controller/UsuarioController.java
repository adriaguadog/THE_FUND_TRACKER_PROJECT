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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.fund_tracker_project.dao.ActivoDao;
import org.example.fund_tracker_project.dao.LineaCarteraDao;
import org.example.fund_tracker_project.dao.OperacionDao;
import org.example.fund_tracker_project.model.*;
import org.example.fund_tracker_project.service.AlertCreation;
import org.example.fund_tracker_project.service.GestorAPI;
import org.example.fund_tracker_project.service.SceneCreation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UsuarioController implements Initializable {

    @FXML
    public Button btnNuevoFondo;

    @FXML
    public Button btnCerrarBuscador;

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

    private LineaCarteraDao lineaCarteraDao;

    private OperacionDao operacionDao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        activoDao = new ActivoDao();
        lineaCarteraDao = new LineaCarteraDao();
        operacionDao = new OperacionDao();
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
            System.out.println("Activos en la lista: " + activosObservable.size());

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
                    mostrarDialogoOperacion(newVal);
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

        btnAddFondo.setOnAction(event -> {
            stackBuscador.setVisible(true);

            txtBuscarActivo.requestFocus();
            txtBuscarActivo.clear();
            listViewActivos.refresh();
        });

        btnNuevoFondo.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nuevo fondo");
            dialog.setHeaderText("Introduce el ticker del fondo o ETF");
            dialog.setContentText("Ticker:");
            dialog.initOwner(btnNuevoFondo.getScene().getWindow());
            Optional<String> resultado = dialog.showAndWait();

            resultado.ifPresent(ticker -> {
                System.out.println("Ticker introducido correctamente: " + ticker);
            });
        });

        btnCerrarBuscador.setOnAction(event -> ocultarBuscador());
    }


    public List<Activo> crearCatalogo() throws IOException, InterruptedException, SQLException {
        if (activoDao.obtenerTodos().isEmpty()) {
            GestorAPI gestorAPI = new GestorAPI();
            List<Activo> listaActivos = gestorAPI.llamarAPI();
            for (Activo activo : listaActivos) {
                try {
                    activoDao.insertarActivo(activo);
                } catch (SQLException e) {
                    // fila duplicada o inválida: se salta y se sigue con la siguiente
                }
            }

            return listaActivos;
        } else {
            return activoDao.obtenerTodos();
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            lblSaludo.setText("Hola, " + usuario.getNombre());
        }
        try {
            operacionDao.obtenerHistoricoUsuario(usuario.getIdUsuario());
        } catch (SQLException e) {
            AlertCreation.crearFallo("Error", "Error de conexion a la base de datos");
        }
    }

    private void ocultarBuscador() {
        stackBuscador.setVisible(false);
        listViewActivos.getSelectionModel().clearSelection();
        txtBuscarActivo.clear();
    }

    private void mostrarDialogoOperacion(Activo activo) {
        Dialog<ButtonType> dialog = new Dialog<>();

        String css = getClass()
                .getResource("/org/example/fund_tracker_project/login.css")
                .toExternalForm();

        dialog.getDialogPane().getStylesheets().add(css);
        dialog.getDialogPane().getStyleClass().add("dialog-operacion");

        dialog.setTitle("Registrar operación");
        dialog.setHeaderText("Operación para: " + activo.getTicker());

        ButtonType botonGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonGuardar, ButtonType.CANCEL);

        // Las clases CSS de los botones se asignan cuando el diálogo ya se está mostrando
        dialog.setOnShown(event -> {
            Button btnGuardar = (Button) dialog.getDialogPane().lookupButton(botonGuardar);
            Button btnCancelar = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

            if (btnGuardar != null) {
                btnGuardar.getStyleClass().add("login-button");
            }
            if (btnCancelar != null) {
                btnCancelar.getStyleClass().add("close-button");
            }
        });

        DatePicker datePickerFecha = new DatePicker(LocalDate.now());

        ComboBox<TipoOperacion> comboTipoOperacion = new ComboBox<>();
        comboTipoOperacion.getItems().addAll(TipoOperacion.values());
        comboTipoOperacion.setValue(TipoOperacion.COMPRA);

        TextField txtParticipaciones = new TextField();
        txtParticipaciones.setPromptText("Ejemplo: 2.5");
        txtParticipaciones.getStyleClass().add("campo-operacion");

        TextField txtPrecioUnitario = new TextField();
        txtPrecioUnitario.setPromptText("Ejemplo: 150.75");
        txtPrecioUnitario.getStyleClass().add("campo-operacion");

        comboTipoOperacion.getStyleClass().add("campo-operacion");
        datePickerFecha.getStyleClass().add("campo-operacion");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("formulario-operacion");
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Fondo:"), 0, 0);
        grid.add(new Label(activo.getNombre() + " (" + activo.getTicker() + ")"), 1, 0);

        grid.add(new Label("Fecha:"), 0, 1);
        grid.add(datePickerFecha, 1, 1);

        grid.add(new Label("Tipo de operación:"), 0, 2);
        grid.add(comboTipoOperacion, 1, 2);

        grid.add(new Label("Participaciones:"), 0, 3);
        grid.add(txtParticipaciones, 1, 3);

        grid.add(new Label("Precio unitario:"), 0, 4);
        grid.add(txtPrecioUnitario, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonGuardar) {
            guardarOperacion(activo, datePickerFecha.getValue(), comboTipoOperacion.getValue(),
                    txtParticipaciones.getText(), txtPrecioUnitario.getText());
        }
        }


    private void guardarOperacion(Activo activo, LocalDate fecha, TipoOperacion tipo,
                                  String textoParticipaciones, String textoPrecio) {
        // campos vacíos?
        if (fecha == null || tipo == null
                || textoParticipaciones.isEmpty() || textoPrecio.isEmpty()) {
            AlertCreation.crearWarning("Campos incompletos", "Debes rellenar todos los campos");
            return;
        }

        // 2. Convertir a números (admite coma decimal: 2,5 -> 2.5)
        double participaciones;
        double precioUnitario;
        try {
            participaciones = Double.parseDouble(textoParticipaciones.replace(",", ".")); // hay que parsearlo porque recoge formato texto
            precioUnitario = Double.parseDouble(textoPrecio.replace(",", ".")); // quiero remplazar las comas por puntos para que no me genere error
        } catch (NumberFormatException e) {
            AlertCreation.crearWarning("Formato incorrecto",
                    "Participaciones y precio deben ser números");
            return;
        }

        //Validar que sean positivos
        if (participaciones <= 0 || precioUnitario <= 0) {
            AlertCreation.crearWarning("Valores incorrectos",
                    "Participaciones y precio deben ser mayores que cero");
            return;
        }

        double importe = participaciones * precioUnitario;

        //Guardar en la BD: primero la línea, después la operación
        try {
            LineaCartera linea = new LineaCartera(usuario.getIdUsuario(), activo.getIdActivo(), importe, participaciones);

            if (tipo == TipoOperacion.VENTA) {
                // Consultar cuántas participaciones tiene realmente a partir de la tabla operaciones con estado ejecutadas
                double participacionesActuales = operacionDao.obtenerParticipacionesEjecutadas(usuario.getIdUsuario(), activo.getIdActivo());

                // Validar antes de tocar nada
                if (participaciones > participacionesActuales) {
                    AlertCreation.crearWarning("Venta no posible", "Solo tienes " + participacionesActuales + " participaciones de este fondo");
                    return;
                }

                lineaCarteraDao.restarLinea(linea);
                //caso de compra o alta
            } else {
                lineaCarteraDao.insertarLinea(linea);
            }

            // La operación se registra siempre (compra o venta)
            Operacion operacion = new Operacion(
                    tipo, importe, 0.0, fecha, participaciones, precioUnitario,
                    usuario.getIdUsuario(), activo.getIdActivo());

            operacion.setEstadoOperacion(EstadoOperacion.EJECUTADA);
            operacionDao.insertarOperacion(operacion);

            AlertCreation.crearInformacion("Operación registrada", activo.getTicker() + " actualizado en tu cartera");

            //actualizo el historico
            operacionDao.obtenerHistoricoUsuario(usuario.getIdUsuario());
        } catch (SQLException e) {
            AlertCreation.crearFallo("Error", "No se pudo guardar la operación: " + e.getMessage());
        }
    }
}