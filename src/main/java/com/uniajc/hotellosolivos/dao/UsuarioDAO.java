package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario iniciarSesion(
            String usuario,
            String contrasena
    ) {

        String sql = """
                SELECT *
                FROM usuarios
                WHERE usuario = ?
                AND contrasena = ?
                AND estado = 'Activo'
                """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario user = new Usuario();
                user.setUsuarioId(rs.getInt("usuario_id"));
                user.setNombre(rs.getString("nombre"));
                user.setUsuario(rs.getString("usuario"));
                user.setContrasena(rs.getString("contrasena"));
                user.setRol(rs.getString("rol"));
                user.setEstado(rs.getString("estado"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario buscarPorUsuario(String usuario) {

        String sql = """
                SELECT *
                FROM usuarios
                WHERE usuario = ?
                """;
        try {
            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario user = new Usuario();
                user.setUsuarioId(rs.getInt("usuario_id"));
                user.setNombre(rs.getString("nombre"));
                user.setUsuario(rs.getString("usuario"));
                user.setContrasena(rs.getString("contrasena"));
                user.setRol(rs.getString("rol"));
                user.setEstado(rs.getString("estado"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}