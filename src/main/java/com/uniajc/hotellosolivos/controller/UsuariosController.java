package com.uniajc.hotellosolivos.controller;

import com.uniajc.hotellosolivos.dao.UsuarioDAO;
import com.uniajc.hotellosolivos.model.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class UsuariosController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private ComboBox<String> cbRol;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TableView<Usuario> tblUsuarios;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colUsuario;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, String> colEstado;
    @FXML
    private Label lblMensaje;
    private Usuario usuarioSeleccionado;

    private final UsuarioDAO usuarioDAO =
            new UsuarioDAO();

    @FXML
    public void initialize() {

        cargarCombos();
        configurarColumnas();
        cargarUsuarios();
        seleccionarUsuario();

    }

    private void cargarCombos() {

        cbRol.getItems().addAll("Administrador", "Empleado");

        cbEstado.getItems().addAll("Activo", "Inactivo"
        );
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(usuarioDAO.listarUsuarios());
        tblUsuarios.setItems(listaUsuarios);
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().removeAll("mensaje-exito");

        if (!lblMensaje.getStyleClass().contains("mensaje-error")) {
            lblMensaje.getStyleClass().add("mensaje-error");
        }
        lblMensaje.setText(mensaje);
    }

    private void mostrarExito(String mensaje) {

        lblMensaje.getStyleClass().removeAll("mensaje-error");

        if (!lblMensaje.getStyleClass().contains("mensaje-exito")) {
            lblMensaje.getStyleClass().add("mensaje-exito");
        }
        lblMensaje.setText(mensaje);
    }

    @FXML
    private void guardarUsuario() {

        if (txtNombre.getText().isBlank()
                || txtUsuario.getText().isBlank()
                || txtContrasena.getText().isBlank()
                || cbRol.getValue() == null
                || cbEstado.getValue() == null) {

            mostrarError("Debe completar todos los campos.");

            return;
        }

        Usuario existente = usuarioDAO.buscarPorUsuario(txtUsuario.getText());

        if (existente != null) {

            mostrarError("El nombre de usuario ya existe.");

            return;
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(txtNombre.getText());
        usuario.setUsuario(txtUsuario.getText());
        usuario.setContrasena(txtContrasena.getText());
        usuario.setRol(cbRol.getValue());
        usuario.setEstado(cbEstado.getValue());

        boolean guardado = usuarioDAO.guardarUsuario(usuario);

        if (guardado) {
            mostrarExito("Usuario registrado correctamente.");
            cargarUsuarios();

        } else {
            mostrarError("No fue posible registrar el usuario.");
        }
    }

    private void seleccionarUsuario() {

        tblUsuarios.getSelectionModel().selectedItemProperty().addListener(
                        (observable,
                         valorAnterior,
                         usuario) -> {

                            if (usuario != null) {
                                usuarioSeleccionado = usuario;
                                txtNombre.setText(usuario.getNombre());
                                txtUsuario.setText(usuario.getUsuario());
                                txtContrasena.setText(usuario.getContrasena());
                                cbRol.setValue(usuario.getRol());
                                cbEstado.setValue(usuario.getEstado());
                            }
                        }
                );
    }

    @FXML
    private void actualizarUsuario() {

        if (usuarioSeleccionado == null) {
            mostrarError("Debe seleccionar un usuario.");

            return;
        }

        if (txtNombre.getText().isBlank()
                || txtUsuario.getText().isBlank()
                || txtContrasena.getText().isBlank()
                || cbRol.getValue() == null
                || cbEstado.getValue() == null) {

            mostrarError("Debe completar todos los campos.");

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea actualizar este usuario?");
        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {

            return;
        }

        if (usuarioSeleccionado.getUsuario().equals("admin") && cbEstado.getValue().equals("Inactivo")) {

            mostrarError("No es posible desactivar el administrador principal.");

            return;
        }

        usuarioSeleccionado.setNombre(txtNombre.getText());
        usuarioSeleccionado.setUsuario(txtUsuario.getText());
        usuarioSeleccionado.setContrasena(txtContrasena.getText());
        usuarioSeleccionado.setRol(cbRol.getValue());
        usuarioSeleccionado.setEstado(cbEstado.getValue());

        boolean actualizado = usuarioDAO.actualizarUsuario(usuarioSeleccionado);

        if (actualizado) {

            mostrarExito("Usuario actualizado correctamente.");

            cargarUsuarios();

        } else {
            mostrarError("No fue posible actualizar el usuario.");
        }
    }

    @FXML
    private void limpiarFormulario() {

        txtNombre.clear();
        txtUsuario.clear();
        txtContrasena.clear();
        cbRol.setValue(null);
        cbEstado.setValue(null);
        usuarioSeleccionado = null;
        tblUsuarios.getSelectionModel().clearSelection();
        lblMensaje.setText("");

    }



}