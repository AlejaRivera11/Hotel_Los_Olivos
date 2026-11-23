package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Usuario;
import com.uniajc.hotellosolivos.util.SeguridadUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private UsuarioDAO usuarioDAO;
    private String nombreUsuarioPrueba;

    @BeforeEach
    void preparar() {
        usuarioDAO = new UsuarioDAO();
        nombreUsuarioPrueba = "test_" + System.currentTimeMillis();
    }

    @AfterEach
    void limpiar() {
        String sql = "DELETE FROM usuarios WHERE usuario = ?";

        try (PreparedStatement ps = ConexionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombreUsuarioPrueba);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Usuario crearUsuarioDePrueba(String rol, String estado) {
        Usuario u = new Usuario();
        u.setNombre("Usuario de Prueba");
        u.setUsuario(nombreUsuarioPrueba);
        u.setContrasena(SeguridadUtil.hashear("123456"));
        u.setRol(rol);
        u.setEstado(estado);
        return u;
    }


    @Test
    void listarUsuariosRegistrados() {
        ArrayList<Usuario> usuarios = usuarioDAO.listarUsuarios();

        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
        assertTrue(usuarios.stream().anyMatch(u -> u.getUsuario().equals("admin")));
    }

    @Test
    void registrarUsuarioNuevo() {
        boolean guardado = usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        assertTrue(guardado);
        assertNotNull(usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba));
    }

    @Test
    void asignarRolAdministrador() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Administrador", "Activo"));

        assertEquals("Administrador", usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba).getRol());
    }

    @Test
    void asignarRolEmpleado() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        assertEquals("Empleado", usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba).getRol());
    }

    @Test
    void actualizarDatosDelUsuario() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        Usuario u = usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba);
        u.setNombre("Nombre Modificado");

        assertTrue(usuarioDAO.actualizarUsuario(u));
        assertEquals("Nombre Modificado",
                usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba).getNombre());
    }

    @Test
    void desactivarUsuario() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        Usuario u = usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba);
        u.setEstado("Inactivo");
        usuarioDAO.actualizarUsuario(u);

        assertEquals("Inactivo", usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba).getEstado());
    }

    @Test
    void detectarUsuarioExistenteImpedirDuplicados() {
        assertNull(usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba));

        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        assertNotNull(usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba));
    }


    @Test
    void guardarContrasenaCifrada() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        Usuario guardado = usuarioDAO.buscarPorUsuario(nombreUsuarioPrueba);

        assertNotEquals("123456", guardado.getContrasena());
        assertTrue(guardado.getContrasena().startsWith("$2"));
        assertEquals(60, guardado.getContrasena().length());
    }
}