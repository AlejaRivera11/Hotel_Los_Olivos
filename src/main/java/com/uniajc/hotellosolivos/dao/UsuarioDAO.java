package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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

    public ArrayList<Usuario> listarUsuarios() {

        ArrayList<Usuario> usuarios =
                new ArrayList<>();

        String sql = """
            SELECT *
            FROM usuarios
            ORDER BY usuario_id
            """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Usuario usuario = new Usuario();
                usuario.setUsuarioId(rs.getInt("usuario_id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstado(rs.getString("estado"));
                usuarios.add(usuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public boolean guardarUsuario(Usuario usuario) {

        String sql = """
            INSERT INTO usuarios
            (
                nombre,
                usuario,
                contrasena,
                rol,
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

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getEstado());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }

    public boolean actualizarUsuario(Usuario usuario) {

        String sql = """
            UPDATE usuarios
            SET
                nombre = ?,
                usuario = ?,
                contrasena = ?,
                rol = ?,
                estado = ?
            WHERE usuario_id = ?
            """;

        try {

            Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getEstado());
            ps.setInt(6, usuario.getUsuarioId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;
    }
}