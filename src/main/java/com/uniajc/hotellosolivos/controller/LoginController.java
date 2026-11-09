package com.uniajc.hotellosolivos.controller;

import com.uniajc.hotellosolivos.dao.UsuarioDAO;
import com.uniajc.hotellosolivos.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        lblMensaje.setText("");
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {

        if (txtUsuario.getText().isBlank()
                || txtContrasena.getText().isBlank()) {
            lblMensaje.setText("Debe ingresar usuario y contraseña");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioSistema = usuarioDAO.buscarPorUsuario(txtUsuario.getText());

        if (usuarioSistema == null) {
            lblMensaje.setText("Usuario o contraseña incorrectos");
            return;
        }

        if (usuarioSistema.getEstado().equalsIgnoreCase("Inactivo")) {
            lblMensaje.setText("Usuario Inactivo. Contacte al administrador.");
            return;
        }

        Usuario usuario = usuarioDAO.iniciarSesion(txtUsuario.getText(), txtContrasena.getText());

        if (usuario == null) {
            lblMensaje.setText("Usuario o contraseña incorrectos");
            return;
        }
        lblMensaje.setText("");
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
            Stage loginStage =
                    (Stage) txtUsuario.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}