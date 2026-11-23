package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Huesped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class HuespedDAO {

    public ArrayList<Huesped> listarHuespedes() {

        ArrayList<Huesped> huespedes = new ArrayList<>();

        String sql = """
                SELECT *
                FROM huespedes
                ORDER BY nombre
                """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Huesped huesped = new Huesped();
                huesped.setHuespedId(rs.getInt("huesped_id"));
                huesped.setCedula(rs.getString("cedula"));
                huesped.setNombre(rs.getString("nombre"));
                huesped.setTelefono(rs.getString("telefono"));
                huesped.setTipoHuesped(rs.getString("tipo_huesped"));
                huesped.setEstado(rs.getString("estado"));
                huespedes.add(huesped);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return huespedes;
    }

    public boolean guardarHuesped(Huesped huesped) {

        String sql = """
            INSERT INTO huespedes
            (
                cedula,
                nombre,
                telefono,
                tipo_huesped,
                estado
            )
            VALUES
            (
                ?, ?, ?, ?, ?
            )
            """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, huesped.getCedula());
            ps.setString(2, huesped.getNombre());
            ps.setString(3, huesped.getTelefono());
            ps.setString(4, huesped.getTipoHuesped());
            ps.setString(5, huesped.getEstado());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

    public boolean actualizarHuesped(
            Huesped huesped
    ) {

        String sql = """
            UPDATE huespedes
            SET
                cedula = ?,
                nombre = ?,
                telefono = ?,
                tipo_huesped = ?,
                estado = ?
            WHERE huesped_id = ?
            """;

        try {

            Connection connection = ConexionBD.getConnection();

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, huesped.getCedula());
            ps.setString(2, huesped.getNombre());
            ps.setString(3, huesped.getTelefono());
            ps.setString(4, huesped.getTipoHuesped());
            ps.setString(5, huesped.getEstado());
            ps.setInt(6, huesped.getHuespedId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

    public Huesped buscarPorCedula(String cedula) {

        String sql = """
            SELECT *
            FROM huespedes
            WHERE cedula = ?
            """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Huesped huesped = new Huesped();

                huesped.setHuespedId(rs.getInt("huesped_id"));
                huesped.setCedula(rs.getString("cedula"));
                huesped.setNombre(rs.getString("nombre"));
                huesped.setTelefono(rs.getString("telefono"));
                huesped.setTipoHuesped(rs.getString("tipo_huesped"));
                huesped.setEstado(rs.getString("estado"));

                return huesped;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}