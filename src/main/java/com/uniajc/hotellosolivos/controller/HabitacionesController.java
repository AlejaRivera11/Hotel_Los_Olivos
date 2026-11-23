package com.uniajc.hotellosolivos.controller;

import com.uniajc.hotellosolivos.dao.HabitacionDAO;
import com.uniajc.hotellosolivos.model.Habitacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class HabitacionesController {

    @FXML
    private TableView<Habitacion> tblHabitaciones;

    @FXML
    private TableColumn<Habitacion, Integer> colNumero;

    @FXML
    private TableColumn<Habitacion, String> colEstado;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private TextField txtNumero;

    @FXML
    private Label lblMensaje;

    private Habitacion habitacionSeleccionada;

    private final HabitacionDAO habitacionDAO =
            new HabitacionDAO();

    @FXML
    public void initialize() {

        cargarEstados();

        configurarColumnas();

        cargarHabitaciones();

        seleccionarHabitacion();

    }

    private void cargarEstados() {

        cbEstado.getItems().addAll(
                "Disponible",
                "Ocupada",
                "En reparación",
                "Fuera de servicio"
        );

    }

    private void configurarColumnas() {

        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

    }

    private void cargarHabitaciones() {

        HabitacionDAO habitacionDAO =
                new HabitacionDAO();

        ObservableList<Habitacion> lista = FXCollections.observableArrayList(habitacionDAO.listarHabitaciones());

        tblHabitaciones.setItems(lista);

    }

    private void seleccionarHabitacion() {

        tblHabitaciones.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         anterior,
                         habitacion) -> {

                            if (habitacion != null) {

                                habitacionSeleccionada = habitacion;
                                txtNumero.setText(String.valueOf(habitacion.getNumero()));
                                cbEstado.setValue(habitacion.getEstado());

                            }

                        }
                );

    }

    private void mostrarError(String mensaje) {

        lblMensaje.getStyleClass().remove("mensaje-exito");

        if (!lblMensaje.getStyleClass().contains("mensaje-error")) {

            lblMensaje.getStyleClass().add("mensaje-error");

        }

        lblMensaje.setText(mensaje);
    }

    private void mostrarExito(String mensaje) {

        lblMensaje.getStyleClass().remove("mensaje-error");

        if (!lblMensaje.getStyleClass().contains("mensaje-exito")) {

            lblMensaje.getStyleClass().add("mensaje-exito");

        }

        lblMensaje.setText(mensaje);
    }

    @FXML
    private void actualizarEstado() {

        if (habitacionSeleccionada == null) {

            mostrarError("Debe seleccionar una habitación.");

            return;
        }

        if (cbEstado.getValue() == null) {

            mostrarError("Debe seleccionar un estado.");

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmación");

        alert.setHeaderText(null);

        alert.setContentText("¿Desea actualizar el estado de la habitación "
                        + habitacionSeleccionada.getNumero()
                        + "?"
        );

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {

            return;
        }

        habitacionSeleccionada.setEstado(cbEstado.getValue());

        boolean actualizado =
                habitacionDAO.actualizarEstado(habitacionSeleccionada);

        if (actualizado) {

            mostrarExito("Estado actualizado correctamente.");

            cargarHabitaciones();

            tblHabitaciones.getSelectionModel().clearSelection();
            txtNumero.clear();
            cbEstado.setValue(null);
            habitacionSeleccionada = null;

        } else {

            mostrarError("No fue posible actualizar el estado.");

        }

    }


}
