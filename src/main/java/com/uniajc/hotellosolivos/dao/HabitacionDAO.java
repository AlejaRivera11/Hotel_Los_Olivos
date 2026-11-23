package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Habitacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class HabitacionDAO {

    public ArrayList<Habitacion> listarHabitaciones() {

        ArrayList<Habitacion> habitaciones = new ArrayList<>();

        String sql = """
                SELECT *
                FROM habitaciones
                ORDER BY numero
                """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Habitacion habitacion = new Habitacion();
                habitacion.setHabitacionId(rs.getInt("habitacion_id"));
                habitacion.setNumero(rs.getInt("numero"));
                habitacion.setEstado(rs.getString("estado"));

                habitaciones.add(habitacion);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return habitaciones;
    }

    public boolean actualizarEstado(Habitacion habitacion
    ) {

        String sql = """
            UPDATE habitaciones
            SET estado = ?
            WHERE habitacion_id = ?
            """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, habitacion.getEstado());
            ps.setInt(2, habitacion.getHabitacionId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

}
