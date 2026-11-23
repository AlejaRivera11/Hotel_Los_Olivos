package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Huesped;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HuespedTest {

    private HuespedDAO huespedDAO;
    private String cedulaPrueba;

    @BeforeEach
    void preparar() {
        huespedDAO = new HuespedDAO();
        cedulaPrueba = "99" + System.currentTimeMillis();
    }

    @AfterEach
    void limpiar() {

        String sql = "DELETE FROM huespedes WHERE cedula = ?";

        try (PreparedStatement ps = ConexionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, cedulaPrueba);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Huesped crearHuespedPrueba() {

        Huesped huesped = new Huesped();

        huesped.setCedula(cedulaPrueba);
        huesped.setNombre("Carlos Prueba");
        huesped.setTelefono("3001111111");
        huesped.setTipoHuesped("Fijo");
        huesped.setEstado("Activo");

        return huesped;
    }

    @Test
    void listarHuespedes() {

        ArrayList<Huesped> huespedes = huespedDAO.listarHuespedes();
        assertNotNull(huespedes);
        assertFalse(huespedes.isEmpty());
    }

    @Test
    void registrarHuespedNuevo() {

        boolean guardado = huespedDAO.guardarHuesped(crearHuespedPrueba());

        assertTrue(guardado);

        assertNotNull(huespedDAO.buscarPorCedula(cedulaPrueba));
    }

    @Test
    void encontrarCedulaExistente() {

        huespedDAO.guardarHuesped(crearHuespedPrueba());
        Huesped huesped = huespedDAO.buscarPorCedula(cedulaPrueba);
        assertNotNull(huesped);
    }

    @Test
    void retornarNullCuandoCedulaNoExiste() {

        Huesped huesped = huespedDAO.buscarPorCedula("000000000");

        assertNull(huesped);
    }

    @Test
    void actualizarNombreDelHuesped() {

        huespedDAO.guardarHuesped(crearHuespedPrueba());
        Huesped huesped = huespedDAO.buscarPorCedula(cedulaPrueba);
        huesped.setNombre("Nombre Modificado");
        assertTrue(huespedDAO.actualizarHuesped(huesped));
        Huesped actualizado = huespedDAO.buscarPorCedula(cedulaPrueba);
        assertEquals("Nombre Modificado", actualizado.getNombre());
    }

    @Test
    void actualizarTelefonoDelHuesped() {

        huespedDAO.guardarHuesped(crearHuespedPrueba());
        Huesped huesped = huespedDAO.buscarPorCedula(cedulaPrueba);
        huesped.setTelefono("3112223333");
        assertTrue(huespedDAO.actualizarHuesped(huesped));
        assertEquals("3112223333", huespedDAO.buscarPorCedula(cedulaPrueba).getTelefono());
    }

    @Test
    void actualizarTipoDeHuesped() {

        huespedDAO.guardarHuesped(crearHuespedPrueba());
        Huesped huesped = huespedDAO.buscarPorCedula(cedulaPrueba);
        huesped.setTipoHuesped("DePaso");
        assertTrue(huespedDAO.actualizarHuesped(huesped));
        assertEquals("DePaso", huespedDAO.buscarPorCedula(cedulaPrueba).getTipoHuesped());
    }

    @Test
    void actualizarEstadoDelHuesped() {

        huespedDAO.guardarHuesped(crearHuespedPrueba());
        Huesped huesped = huespedDAO.buscarPorCedula(cedulaPrueba);
        huesped.setEstado("Inactivo");
        assertTrue(huespedDAO.actualizarHuesped(huesped));
        assertEquals("Inactivo", huespedDAO.buscarPorCedula(cedulaPrueba).getEstado());
    }

    @Test
    void actualizarHuespedInexistente() {

        Huesped huesped = crearHuespedPrueba();
        huesped.setHuespedId(-1);
        assertFalse(huespedDAO.actualizarHuesped(huesped));
    }
}