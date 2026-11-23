package com.uniajc.hotellosolivos.controller;

import com.uniajc.hotellosolivos.dao.HuespedDAO;
import com.uniajc.hotellosolivos.model.Huesped;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class HuespedesController {

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private ComboBox<String> cbTipoHuesped;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private Label lblMensaje;

    @FXML
    private TableView<Huesped> tblHuespedes;

    @FXML
    private TableColumn<Huesped, String> colCedula;

    @FXML
    private TableColumn<Huesped, String> colNombre;

    @FXML
    private TableColumn<Huesped, String> colTelefono;

    @FXML
    private TableColumn<Huesped, String> colTipoHuesped;

    @FXML
    private TableColumn<Huesped, String> colEstado;

    private final HuespedDAO huespedDAO =
            new HuespedDAO();

    private Huesped huespedSeleccionado;

    @FXML
    public void initialize() {

        cargarCombos();

        configurarColumnas();

        cargarHuespedes();

        seleccionarHuesped();

    }

    private void cargarCombos() {

        cbTipoHuesped.getItems().addAll("Fijo", "DePaso");
        cbEstado.getItems().addAll("Activo", "Inactivo");

    }

    private void configurarColumnas() {

        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTipoHuesped.setCellValueFactory(new PropertyValueFactory<>("tipoHuesped"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

    }

    private void cargarHuespedes() {

        ObservableList<Huesped> lista = FXCollections.observableArrayList(huespedDAO.listarHuespedes());
        tblHuespedes.setItems(lista);
    }

    private void seleccionarHuesped() {

        tblHuespedes.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         anterior,
                         huesped) -> {

                            if (huesped != null) {

                                huespedSeleccionado = huesped;
                                txtCedula.setText(huesped.getCedula());
                                txtNombre.setText(huesped.getNombre());
                                txtTelefono.setText(huesped.getTelefono());
                                cbTipoHuesped.setValue(huesped.getTipoHuesped());
                                cbEstado.setValue(huesped.getEstado());
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
    private void limpiarCampos() {

        txtCedula.clear();

        txtNombre.clear();

        txtTelefono.clear();

        cbTipoHuesped.setValue(null);

        cbEstado.setValue(null);

        tblHuespedes.getSelectionModel()
                .clearSelection();

        huespedSeleccionado = null;
    }

    @FXML
    private void guardarHuesped() {

        if (txtCedula.getText().isBlank()
                || txtNombre.getText().isBlank()
                || txtTelefono.getText().isBlank()
                || cbTipoHuesped.getValue() == null
                || cbEstado.getValue() == null) {

            mostrarError("Debe completar todos los campos.");
            return;
        }

        Huesped existente = huespedDAO.buscarPorCedula(txtCedula.getText());

        if (existente != null) {

            mostrarError("La cédula ya está registrada.");

            return;
        }

        Huesped huesped = new Huesped();

        huesped.setCedula(txtCedula.getText());
        huesped.setNombre(txtNombre.getText());
        huesped.setTelefono(txtTelefono.getText());
        huesped.setTipoHuesped(cbTipoHuesped.getValue());
        huesped.setEstado(cbEstado.getValue());

        boolean guardado = huespedDAO.guardarHuesped(huesped);

        if (guardado) {

            mostrarExito("Huésped registrado correctamente.");

            cargarHuespedes();

            limpiarCampos();

        } else {

            mostrarError("No fue posible registrar el huésped.");

        }
    }

    @FXML
    private void actualizarHuesped() {

        if (huespedSeleccionado == null) {

            mostrarError("Debe seleccionar un huésped.");

            return;
        }

        if (txtCedula.getText().isBlank()
                || txtNombre.getText().isBlank()
                || txtTelefono.getText().isBlank()
                || cbTipoHuesped.getValue() == null
                || cbEstado.getValue() == null) {

            mostrarError("Debe completar todos los campos.");

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmación");

        alert.setHeaderText(null);

        alert.setContentText("¿Desea actualizar este huésped?"           );

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {

            return;
        }

        huespedSeleccionado.setCedula(txtCedula.getText());
        huespedSeleccionado.setNombre(txtNombre.getText());
        huespedSeleccionado.setTelefono(txtTelefono.getText());
        huespedSeleccionado.setTipoHuesped(cbTipoHuesped.getValue());
        huespedSeleccionado.setEstado(cbEstado.getValue());

        boolean actualizado = huespedDAO.actualizarHuesped(huespedSeleccionado);

        if (actualizado) {

            mostrarExito("Huésped actualizado correctamente.");

            cargarHuespedes();

            limpiarCampos();

        } else {

            mostrarError("No fue posible actualizar el huésped.");

        }

    }





}


