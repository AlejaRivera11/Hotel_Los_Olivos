package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.model.Habitacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HabitacionTest {

    private HabitacionDAO habitacionDAO;
    private Habitacion habitacionPrueba;
    private String estadoOriginal;

    private static final List<String> ESTADOS_VALIDOS = List.of(
            "Disponible", "Ocupada", "En reparación", "Fuera de servicio"
    );

    @BeforeEach
    void preparar() {
        habitacionDAO = new HabitacionDAO();
        habitacionPrueba = habitacionDAO.listarHabitaciones().get(0);
        estadoOriginal = habitacionPrueba.getEstado();
    }

    @AfterEach
    void restaurar() {
        habitacionPrueba.setEstado(estadoOriginal);
        habitacionDAO.actualizarEstado(habitacionPrueba);
    }

    private Habitacion buscarPorId(int id) {
        return habitacionDAO.listarHabitaciones().stream()
                .filter(h -> h.getHabitacionId() == id)
                .findFirst()
                .orElse(null);
    }

    private void cambiarYVerificar(String nuevoEstado) {
        habitacionPrueba.setEstado(nuevoEstado);

        assertTrue(habitacionDAO.actualizarEstado(habitacionPrueba));
        assertEquals(nuevoEstado, buscarPorId(habitacionPrueba.getHabitacionId()).getEstado());
    }

    @Test
    void listarLasHabitaciones() {
        ArrayList<Habitacion> habitaciones = habitacionDAO.listarHabitaciones();

        assertEquals(14, habitaciones.size());
    }

    @Test
    void mostrarNumeroIdentificadorUnicoDeLaHabitacion() {
        Set<Integer> numeros = habitacionDAO.listarHabitaciones().stream()
                .map(Habitacion::getNumero)
                .collect(Collectors.toSet());

        assertEquals(14, numeros.size());
        assertTrue(numeros.containsAll(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)));
    }

    @Test
    void listarLasHabitacionesOrdenadasPorNumero() {
        ArrayList<Habitacion> habitaciones = habitacionDAO.listarHabitaciones();

        for (int i = 1; i < habitaciones.size(); i++) {
            assertTrue(habitaciones.get(i - 1).getNumero() < habitaciones.get(i).getNumero());
        }
    }

    @Test
    void todasLasHabitacionesDebenTenerUnEstadoValido() {
        for (Habitacion h : habitacionDAO.listarHabitaciones()) {
            assertTrue(ESTADOS_VALIDOS.contains(h.getEstado()),
                    "Estado inválido en la habitación " + h.getNumero() + ": " + h.getEstado());
        }
    }


    @Test
    void cambiarEstadoADisponible() {

        cambiarYVerificar("Disponible");
    }

    @Test
    void cambiarEstadoAOcupada() {

        cambiarYVerificar("Ocupada");
    }

    @Test
    void cambiarEstadoAEnReparacion() {

        cambiarYVerificar("En reparación");
    }

    @Test
    void cambiarEstadoAFueraDeServicio() {

        cambiarYVerificar("Fuera de servicio");
    }

    @Test
    void actualizarUnaHabitacionInexistente() {
        Habitacion inexistente = new Habitacion();
        inexistente.setHabitacionId(-1);
        inexistente.setEstado("Disponible");

        assertFalse(habitacionDAO.actualizarEstado(inexistente));
    }
}
