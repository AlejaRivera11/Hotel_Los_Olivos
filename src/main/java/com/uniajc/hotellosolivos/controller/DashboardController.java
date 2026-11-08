package com.uniajc.hotellosolivos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnHabitaciones;

    @FXML
    private Button btnHuespedes;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    public void abrirUsuarios(ActionEvent event){

        System.out.println("Abrir módulo Usuarios");

    }

    @FXML
    public void abrirHabitaciones(ActionEvent event){

        System.out.println("Abrir módulo Habitaciones");

    }

    @FXML
    public void abrirHuespedes(ActionEvent event){

        System.out.println("Abrir módulo Huéspedes");

    }

    @FXML
    public void cerrarSesion(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/view/Login.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(
                    "Inicio de Sesión"
            );

            stage.show();

            Stage dashboardStage =
                    (Stage) btnCerrarSesion
                            .getScene()
                            .getWindow();

            dashboardStage.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
